package co.jinear.core.mcp;

import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.file.ListFilesTool;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialHierarchyDto;
import co.jinear.core.model.dto.material.PathAwareMaterialDto;
import co.jinear.core.model.enumtype.material.MaterialSearchSortType;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaNode;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.response.material.ParentMaterialDtoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class McpListFilesToolTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MaterialListingManager materialListingManager = Mockito.mock(MaterialListingManager.class);
    private final ListFilesTool tool = new ListFilesTool(materialListingManager, new McpViewConverter());

    @BeforeEach
    void setUp() {
        MaterialHierarchyDto hierarchy = new MaterialHierarchyDto();
        hierarchy.setContent(new PageDto<>(Page.empty()));
        ParentMaterialDtoResponse response = new ParentMaterialDtoResponse();
        response.setMaterialHierarchyDto(hierarchy);
        Mockito.when(materialListingManager.search(Mockito.any(MaterialSearchRequest.class))).thenReturn(response);
    }

    @Test
    void theSortArgumentIsPassedToTheListing() throws Exception {
        tool.call(context(), args("{\"workspaceId\":\"ws\",\"sort\":\"SIZE_DESC\"}"));

        assertThat(capturedRequest().getMaterialSearchSortType()).isEqualTo(MaterialSearchSortType.SIZE_DESC);
    }

    @Test
    void newestFirstStaysTheOrderWhenNoSortIsGiven() throws Exception {
        tool.call(context(), args("{\"workspaceId\":\"ws\"}"));

        assertThat(capturedRequest().getMaterialSearchSortType()).isEqualTo(MaterialSearchSortType.IDATE_DESC);
    }

    @Test
    void theSortArgumentPublishesItsAllowedValues() {
        McpSchemaNode sort = tool.definition().getInputSchema().getProperties().get("sort");

        assertThat(sort.getEnumValues()).containsExactly(
                "IDATE_DESC", "IDATE_ASC", "UDATE_DESC", "UDATE_ASC", "NAME_ASC", "NAME_DESC", "SIZE_ASC", "SIZE_DESC");
    }

    @Test
    void aFileIdAsTheFolderIsRefusedInsteadOfAnEmptyList() throws Exception {
        PathAwareMaterialDto container = new PathAwareMaterialDto();
        container.setMaterialType(MaterialType.FILE);
        MaterialHierarchyDto hierarchy = new MaterialHierarchyDto();
        hierarchy.setContainer(container);
        hierarchy.setContent(new PageDto<>(Page.empty()));
        ParentMaterialDtoResponse response = new ParentMaterialDtoResponse();
        response.setMaterialHierarchyDto(hierarchy);
        Mockito.when(materialListingManager.search(Mockito.any(MaterialSearchRequest.class))).thenReturn(response);

        McpToolResult result = tool.call(context(), args("{\"workspaceId\":\"ws\",\"parentFolderId\":\"file-1\"}"));

        assertThat(result.isError()).isTrue();
        assertThat(result.getText()).isEqualTo("mcp.error.file.is-file");
    }

    @Test
    void aFolderIdListsItsContents() throws Exception {
        PathAwareMaterialDto container = new PathAwareMaterialDto();
        container.setMaterialType(MaterialType.FOLDER);
        MaterialHierarchyDto hierarchy = new MaterialHierarchyDto();
        hierarchy.setContainer(container);
        hierarchy.setContent(new PageDto<>(Page.empty()));
        ParentMaterialDtoResponse response = new ParentMaterialDtoResponse();
        response.setMaterialHierarchyDto(hierarchy);
        Mockito.when(materialListingManager.search(Mockito.any(MaterialSearchRequest.class))).thenReturn(response);

        McpToolResult result = tool.call(context(), args("{\"workspaceId\":\"ws\",\"parentFolderId\":\"folder-1\"}"));

        assertThat(result.isError()).isFalse();
    }

    private MaterialSearchRequest capturedRequest() {
        ArgumentCaptor<MaterialSearchRequest> captor = ArgumentCaptor.forClass(MaterialSearchRequest.class);
        Mockito.verify(materialListingManager).search(captor.capture());
        return captor.getValue();
    }

    private McpToolArguments args(String json) throws Exception {
        return McpToolArguments.of(OBJECT_MAPPER.readTree(json), OBJECT_MAPPER, Validation.buildDefaultValidatorFactory().getValidator());
    }

    private McpToolContext context() {
        return McpToolContext.builder().accountId("acc").scopes(Set.of()).build();
    }
}
