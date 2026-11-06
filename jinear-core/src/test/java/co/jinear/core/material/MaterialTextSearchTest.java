package co.jinear.core.material;

import co.jinear.core.converter.material.MaterialDtoConverter;
import co.jinear.core.converter.material.MaterialSearchRequestToVoConverter;
import co.jinear.core.converter.material.MaterialTextSearchRequestToVoConverterImpl;
import co.jinear.core.converter.mcp.McpViewConverter;
import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.manager.mcp.tool.McpToolArguments;
import co.jinear.core.manager.mcp.tool.file.SearchFilesTool;
import co.jinear.core.model.dto.PageDto;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.entity.material.MaterialFts;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.response.material.MaterialPaginatedResponse;
import co.jinear.core.model.vo.material.MaterialFtsSearchVo;
import co.jinear.core.repository.material.MaterialFtsSearchRepository;
import co.jinear.core.repository.material.MaterialSearchRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.material.MaterialSearchService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

class MaterialTextSearchTest {

    private final MaterialFtsSearchRepository materialFtsSearchRepository = Mockito.mock(MaterialFtsSearchRepository.class);
    private final MaterialRetrieveService materialRetrieveService = Mockito.mock(MaterialRetrieveService.class);
    private final MaterialSearchService materialSearchService = new MaterialSearchService(
            Mockito.mock(MaterialSearchRepository.class),
            Mockito.mock(MaterialDtoConverter.class),
            materialFtsSearchRepository,
            materialRetrieveService);

    @Test
    void aBlankQueryReturnsAnEmptyPageWithoutAQuery() {
        Page<MaterialDto> result = materialSearchService.searchText(searchVo("   ", null));

        assertThat(result.getContent()).isEmpty();
        Mockito.verifyNoInteractions(materialFtsSearchRepository, materialRetrieveService);
    }

    @Test
    void materialsComeBackInRankOrderFromOneBatchRead() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<MaterialFts> ranked = new PageImpl<>(List.of(fts("m3"), fts("m1"), fts("m2")), pageable, 3);
        Mockito.when(materialFtsSearchRepository.search(anyString(), anyString(), isNull(), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(ranked);
        Mockito.when(materialRetrieveService.retrieveAll(List.of("m3", "m1", "m2")))
                .thenReturn(List.of(material("m1"), material("m2"), material("m3")));

        Page<MaterialDto> result = materialSearchService.searchText(searchVo("report", null));

        assertThat(result.getContent()).extracting(MaterialDto::getMaterialId).containsExactly("m3", "m1", "m2");
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void aMaterialDeletedBetweenTheTwoReadsIsLeftOut() {
        Pageable pageable = PageRequest.of(0, 50);
        Page<MaterialFts> ranked = new PageImpl<>(List.of(fts("m1"), fts("gone")), pageable, 2);
        Mockito.when(materialFtsSearchRepository.search(anyString(), anyString(), isNull(), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(ranked);
        Mockito.when(materialRetrieveService.retrieveAll(List.of("m1", "gone")))
                .thenReturn(List.of(material("m1")));

        Page<MaterialDto> result = materialSearchService.searchText(searchVo("report", null));

        assertThat(result.getContent()).extracting(MaterialDto::getMaterialId).containsExactly("m1");
    }

    @Test
    void theTypeFilterIsPassedAsItsStoredValue() {
        Mockito.when(materialFtsSearchRepository.search(anyString(), anyString(), eq(1), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(Page.empty());

        materialSearchService.searchText(searchVo("design", MaterialType.FOLDER));

        Mockito.verify(materialFtsSearchRepository).search(eq("design"), eq("ws"), eq(1), eq("acc"), eq(false), any(Pageable.class));
    }

    @Test
    void managerPassesTheCallerAndTheWorkspaceAdminFlag() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
        MaterialSearchService searchService = Mockito.mock(MaterialSearchService.class);
        MaterialListingManager manager = new MaterialListingManager(
                sessionInfoService,
                Mockito.mock(MaterialAccessValidationService.class),
                searchService,
                workspaceValidator,
                Mockito.mock(MaterialSearchRequestToVoConverter.class),
                Mockito.mock(MaterialRetrieveService.class),
                new MaterialTextSearchRequestToVoConverterImpl());
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("acc");
        Mockito.when(workspaceValidator.isWorkspaceAdminOrOwner("acc", "ws")).thenReturn(true);
        Mockito.when(searchService.searchText(any(MaterialFtsSearchVo.class))).thenReturn(Page.empty());
        MaterialTextSearchRequest request = new MaterialTextSearchRequest();
        request.setQuery("report");
        request.setMaterialType(MaterialType.FILE);

        manager.searchText("ws", request, 2);

        Mockito.verify(workspaceValidator).validateHasAccess("acc", "ws");
        ArgumentCaptor<MaterialFtsSearchVo> captor = ArgumentCaptor.forClass(MaterialFtsSearchVo.class);
        Mockito.verify(searchService).searchText(captor.capture());
        MaterialFtsSearchVo vo = captor.getValue();
        assertThat(vo.getQuery()).isEqualTo("report");
        assertThat(vo.getMaterialType()).isEqualTo(MaterialType.FILE);
        assertThat(vo.getWorkspaceId()).isEqualTo("ws");
        assertThat(vo.getAccountId()).isEqualTo("acc");
        assertThat(vo.isWorkspaceAdmin()).isTrue();
        assertThat(vo.getPage()).isEqualTo(2);
    }

    @Test
    void searchFilesPassesTheQueryTypeAndPageAndReturnsFileViews() throws Exception {
        MaterialListingManager manager = Mockito.mock(MaterialListingManager.class);
        MaterialPaginatedResponse response = new MaterialPaginatedResponse();
        response.setMaterialDtoPageDto(new PageDto<>(new PageImpl<>(List.of(material("m1")))));
        Mockito.when(manager.searchText(eq("ws"), any(MaterialTextSearchRequest.class), eq(1))).thenReturn(response);
        SearchFilesTool tool = new SearchFilesTool(manager, new McpViewConverter());
        ObjectMapper objectMapper = new ObjectMapper();
        McpToolArguments args = McpToolArguments.of(
                objectMapper.readTree("{\"workspaceId\":\"ws\",\"query\":\"report\",\"type\":\"FILE\",\"page\":1}"),
                objectMapper,
                Validation.buildDefaultValidatorFactory().getValidator());
        McpToolContext context = McpToolContext.builder().accountId("acc").scopes(Set.of()).build();

        McpToolResult result = tool.call(context, args);

        ArgumentCaptor<MaterialTextSearchRequest> captor = ArgumentCaptor.forClass(MaterialTextSearchRequest.class);
        Mockito.verify(manager).searchText(eq("ws"), captor.capture(), eq(1));
        assertThat(captor.getValue().getQuery()).isEqualTo("report");
        assertThat(captor.getValue().getMaterialType()).isEqualTo(MaterialType.FILE);
        assertThat(result.isError()).isFalse();
        assertThat(context.getWorkspaceId()).isEqualTo("ws");
    }

    private MaterialFtsSearchVo searchVo(String query, MaterialType materialType) {
        MaterialFtsSearchVo vo = new MaterialFtsSearchVo();
        vo.setQuery(query);
        vo.setMaterialType(materialType);
        vo.setWorkspaceId("ws");
        vo.setAccountId("acc");
        return vo;
    }

    private MaterialFts fts(String materialId) {
        MaterialFts materialFts = new MaterialFts();
        materialFts.setMaterialId(materialId);
        return materialFts;
    }

    private MaterialDto material(String materialId) {
        MaterialDto materialDto = new MaterialDto();
        materialDto.setMaterialId(materialId);
        materialDto.setWorkspaceId("ws");
        materialDto.setMaterialType(MaterialType.FILE);
        return materialDto;
    }
}
