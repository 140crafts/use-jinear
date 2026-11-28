package co.jinear.core.material;

import co.jinear.core.converter.material.MaterialAccessDtoConverter;
import co.jinear.core.converter.material.MaterialInitializeFileUploadRequestToVoConverter;
import co.jinear.core.converter.material.MaterialInitializeFolderRequestToVoConverter;
import co.jinear.core.exception.NotValidException;
import co.jinear.core.manager.material.MaterialOperationManager;
import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.entity.material.MaterialAccess;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.repository.material.MaterialAccessRepository;
import co.jinear.core.repository.material.MaterialRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessService;
import co.jinear.core.service.material.MaterialAccessValidationService;
import co.jinear.core.service.material.MaterialLockService;
import co.jinear.core.service.material.MaterialOperationService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.validator.workspace.WorkspaceMediaLimitQueryService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaterialOwnerAccessTest {

    private final MaterialAccessRepository materialAccessRepository = Mockito.mock(MaterialAccessRepository.class);
    private final MaterialAccessService materialAccessService = new MaterialAccessService(
            Mockito.mock(MaterialLockService.class), materialAccessRepository, Mockito.mock(MaterialAccessDtoConverter.class));

    private final MaterialAccessValidationService materialAccessValidationService = new MaterialAccessValidationService(
            Mockito.mock(MaterialRepository.class), Mockito.mock(MaterialRetrieveService.class),
            Mockito.mock(WorkspaceValidator.class), materialAccessService);

    private MaterialDto material(MaterialAccessType accessType) {
        MaterialDto material = new MaterialDto();
        material.setMaterialId("material-1");
        material.setWorkspaceId("workspace-1");
        material.setOwnerId("owner");
        material.setMaterialAccessType(accessType);
        return material;
    }

    @Test
    void switchingToGrainedGivesTheOwnerAccessEvenWhenAnAdminSwitches() {
        SessionInfoService sessionInfoService = Mockito.mock(SessionInfoService.class);
        MaterialRetrieveService materialRetrieveService = Mockito.mock(MaterialRetrieveService.class);
        MaterialAccessService mockedMaterialAccessService = Mockito.mock(MaterialAccessService.class);
        MaterialOperationManager manager = new MaterialOperationManager(
                sessionInfoService, Mockito.mock(WorkspaceValidator.class), Mockito.mock(MaterialOperationService.class),
                Mockito.mock(MaterialAccessValidationService.class), mockedMaterialAccessService,
                Mockito.mock(MaterialInitializeFolderRequestToVoConverter.class),
                Mockito.mock(MaterialInitializeFileUploadRequestToVoConverter.class), materialRetrieveService,
                Mockito.mock(PassiveService.class), Mockito.mock(WorkspaceMediaLimitQueryService.class));
        Mockito.when(sessionInfoService.currentAccountId()).thenReturn("admin");
        Mockito.when(materialRetrieveService.retrieve("material-1")).thenReturn(material(MaterialAccessType.OWNER_ONLY));

        manager.updateAccessType("material-1", MaterialAccessType.GRAINED);

        Mockito.verify(mockedMaterialAccessService).giveOwnerAccessIfGrained("material-1", "owner", MaterialAccessType.GRAINED);
    }

    @Test
    void grainedAccessTypeStoresAnOwnerAccessRow() {
        materialAccessService.giveOwnerAccessIfGrained("material-1", "owner", MaterialAccessType.GRAINED);

        ArgumentCaptor<MaterialAccess> captor = ArgumentCaptor.forClass(MaterialAccess.class);
        Mockito.verify(materialAccessRepository).save(captor.capture());
        assertThat(captor.getValue().getAccountId()).isEqualTo("owner");
        assertThat(captor.getValue().getMaterialId()).isEqualTo("material-1");
    }

    @Test
    void grainedAccessTypeDoesNotDuplicateAnExistingOwnerAccessRow() {
        Mockito.when(materialAccessRepository.existsByMaterialIdAndAccountIdAndPassiveIdIsNull("material-1", "owner")).thenReturn(true);

        materialAccessService.giveOwnerAccessIfGrained("material-1", "owner", MaterialAccessType.GRAINED);

        Mockito.verify(materialAccessRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    void otherAccessTypesStoreNoOwnerAccessRow() {
        materialAccessService.giveOwnerAccessIfGrained("material-1", "owner", MaterialAccessType.WORKSPACE_MEMBERS);

        Mockito.verifyNoInteractions(materialAccessRepository);
    }

    @Test
    void theOwnersAccessCannotBeRevoked() {
        MaterialDto material = material(MaterialAccessType.GRAINED);

        assertThatThrownBy(() -> materialAccessValidationService.validateAccountIsNotMaterialOwner("owner", material))
                .isInstanceOf(NotValidException.class);
    }

    @Test
    void anotherMembersAccessCanBeRevoked() {
        MaterialDto material = material(MaterialAccessType.GRAINED);

        assertThatCode(() -> materialAccessValidationService.validateAccountIsNotMaterialOwner("member", material))
                .doesNotThrowAnyException();
    }
}
