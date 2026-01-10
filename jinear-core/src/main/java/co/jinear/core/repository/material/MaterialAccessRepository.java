package co.jinear.core.repository.material;

import co.jinear.core.model.entity.material.MaterialAccess;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialAccessRepository extends JpaRepository<MaterialAccess, String> {

    Page<MaterialAccess> findAllByMaterialIdAndPassiveIdIsNullOrderByCreatedDateAsc(String materialId, Pageable pageable);

    boolean existsByMaterialIdAndAccountIdAndPassiveIdIsNull(String materialId, String accountId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update MaterialAccess mc
                set mc.passiveId = :passiveId, mc.lastUpdatedDate = current_timestamp()
                    where
                        mc.accountId = :accountId and
                        mc.materialId = :materialId and
                        mc.passiveId is null
            """)
    void updateByAccountIdAndMaterialId(@Param("accountId") String accountId, @Param("materialId") String materialId, @Param("passiveId") String passiveId);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update MaterialAccess mc
                set mc.passiveId = :passiveId, mc.lastUpdatedDate = current_timestamp()
                    where
                        mc.materialId = :materialId and
                        mc.passiveId is null
            """)
    void updateByMaterialId(@Param("materialId") String materialId, @Param("passiveId") String passiveId);

}
