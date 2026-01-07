package co.jinear.core.service.material;

import co.jinear.core.model.enumtype.lock.LockSourceType;
import co.jinear.core.service.lock.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialLockService {

    private final LockService lockService;

    public void lockMaterialForAccessUpdate(String materialId) {
        log.info("Lock material for access update has started for materialId: {}", materialId);
        lockService.lock(materialId, LockSourceType.MATERIAL_ACCESS_UPDATE);
        log.info("Lock material for access update has completed for materialId: {}", materialId);
    }

    public void unlockMaterialForAccessUpdate(String materialId) {
        log.info("Unlock material for access update has started for materialId: {}", materialId);
        lockService.unlock(materialId, LockSourceType.MATERIAL_ACCESS_UPDATE);
        log.info("Unlock material for access update has completed for materialId: {}", materialId);
    }
}
