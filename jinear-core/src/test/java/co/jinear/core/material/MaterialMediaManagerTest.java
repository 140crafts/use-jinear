package co.jinear.core.material;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.manager.material.MaterialMediaManager;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaterialMediaManagerTest {

    private final SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
    private final MaterialRetrieveService materialRetrieveService = Mockito.mock(MaterialRetrieveService.class);
    private final WorkspaceValidator workspaceValidator = Mockito.mock(WorkspaceValidator.class);
    private final MediaRetrieveService mediaRetrieveService = Mockito.mock(MediaRetrieveService.class);
    private final MaterialAccessValidationService materialAccessValidationService = Mockito.mock(MaterialAccessValidationService.class);
    private final MaterialMediaManager manager = new MaterialMediaManager(
            sessionInfoService, materialRetrieveService, workspaceValidator, mediaRetrieveService, materialAccessValidationService);

    private MaterialDto file(MaterialAccessType accessType) {
        MaterialDto material = new MaterialDto();
        material.setMaterialId("file-1");
        material.setWorkspaceId("workspace-1");
        material.setMaterialType(MaterialType.FILE);
        material.setMaterialAccessType(accessType);
        material.setName("report.pdf");
        return material;
    }

    @Test
    void anAccountOutsideTheWorkspaceCannotDownloadAMembersOnlyFile() {
        Mockito.when(sessionInfoService.currentAccountIdInclAnonymous()).thenReturn("stranger");
        Mockito.when(materialRetrieveService.retrieve("file-1", MaterialType.FILE)).thenReturn(file(MaterialAccessType.WORKSPACE_MEMBERS));
        Mockito.doThrow(new NoAccessException()).when(workspaceValidator).validateHasAccess("stranger", "workspace-1");

        assertThatThrownBy(() -> manager.downloadMaterialMedia(new MockHttpServletResponse(), "file-1"))
                .isInstanceOf(NoAccessException.class);
        Mockito.verifyNoInteractions(mediaRetrieveService);
    }

    @Test
    void anIdThatIsNotAFileIsNotDownloadable() {
        Mockito.when(materialRetrieveService.retrieve("notebook-1", MaterialType.FILE)).thenThrow(new NotFoundException());

        assertThatThrownBy(() -> manager.downloadMaterialMedia(new MockHttpServletResponse(), "notebook-1"))
                .isInstanceOf(NotFoundException.class);
        Mockito.verifyNoInteractions(mediaRetrieveService);
    }

    @Test
    void aFileSharedWithAnyoneWithTheLinkSkipsTheMembershipCheck() throws Exception {
        Mockito.when(materialRetrieveService.retrieve("file-1", MaterialType.FILE)).thenReturn(file(MaterialAccessType.ANYONE_WITH_LINK));
        Mockito.when(mediaRetrieveService.retrievePresignedPublicDownloadLink(ArgumentMatchers.any()))
                .thenReturn(URI.create("https://storage.example/file-1").toURL());
        MockHttpServletResponse response = new MockHttpServletResponse();

        manager.downloadMaterialMedia(response, "file-1");

        assertThat(response.getRedirectedUrl()).isEqualTo("https://storage.example/file-1");
        Mockito.verifyNoInteractions(workspaceValidator, materialAccessValidationService);
    }
}
