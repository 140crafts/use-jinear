package co.jinear.core.service.material;

import co.jinear.core.converter.material.MaterialAccessDtoConverter;
import co.jinear.core.model.dto.material.MaterialAccessDto;
import co.jinear.core.model.entity.material.MaterialAccess;
import co.jinear.core.repository.material.MaterialAccessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialAccessService {

    private static final int PAGE_SIZE = 1000;

    private final MaterialLockService materialLockService;
    private final MaterialAccessRepository materialAccessRepository;
    private final MaterialAccessDtoConverter materialAccessDtoConverter;

    public Page<MaterialAccessDto> retrieveAccessList(String materialId, int page) {
        log.info("Retrieve access list has started. materialId: {}, page: {}", materialId, page);
        return materialAccessRepository.findAllByMaterialIdAndPassiveIdIsNullOrderByCreatedDateAsc(materialId, PageRequest.of(page, PAGE_SIZE))
                .map(materialAccessDtoConverter::map);
    }

    @Transactional
    public void giveAccess(String materialId, String accountId) {
        log.info("Give access has started. materialId: {}, accountId: {}", materialId, accountId);
        materialLockService.lockMaterialForAccessUpdate(materialId);
        try {
            boolean exists = materialAccessRepository.existsByMaterialIdAndAccountIdAndPassiveIdIsNull(materialId, accountId);
            if (!exists) {
                initialize(materialId, accountId);
            }
        } finally {
            materialLockService.unlockMaterialForAccessUpdate(materialId);
        }
    }

    public void revokeAccess(String materialId, String accountId, String passiveId) {
        log.info("Revoke access has started. materialId: {}, accountId: {}", materialId, accountId);
        materialLockService.lockMaterialForAccessUpdate(materialId);
        try {
            materialAccessRepository.updateByAccountIdAndMaterialId(accountId, materialId, passiveId);
        } finally {
            materialLockService.unlockMaterialForAccessUpdate(materialId);
        }
    }

    public void revokeEveryAccess(String materialId, String passiveId) {
        log.info("Revoke every access has started. materialId: {}", materialId);
        materialLockService.lockMaterialForAccessUpdate(materialId);
        try {
            materialAccessRepository.updateByMaterialId(materialId, passiveId);
            log.info("Revoke every access has completed. materialId: {}", materialId);
        } finally {
            materialLockService.unlockMaterialForAccessUpdate(materialId);
        }
    }

    public boolean hasAccess(String materialId, String accountId) {
        log.info("Check access has started. materialId: {}, accountId: {}", materialId, accountId);
        return materialAccessRepository.existsByMaterialIdAndAccountIdAndPassiveIdIsNull(materialId, accountId);
    }

    private void initialize(String materialId, String accountId) {
        MaterialAccess materialAccess = new MaterialAccess();
        materialAccess.setMaterialId(materialId);
        materialAccess.setAccountId(accountId);
        materialAccessRepository.save(materialAccess);
    }
}
