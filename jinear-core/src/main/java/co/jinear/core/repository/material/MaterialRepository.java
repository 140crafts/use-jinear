package co.jinear.core.repository.material;

import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, String> {

    Optional<Material> findByMaterialIdAndPassiveIdIsNull(String materialId);

    boolean existsByMaterialIdAndWorkspaceIdAndPassiveIdIsNull(String materialId, String workspaceId);

    Optional<Material> findByMaterialIdAndMaterialTypeAndPassiveIdIsNull(String materialId, MaterialType materialType);

    boolean existsByMaterialIdAndMaterialTypeAndPassiveIdIsNull(String materialId, MaterialType materialType);

    boolean existsByMaterialIdAndMaterialTypeAndWorkspaceIdAndPassiveIdIsNull(String materialId, MaterialType materialType, String workspaceId);

    boolean existsByWorkspaceIdAndMaterialIdAndOwnerIdAndPassiveIdIsNull(String workspaceId, String materialId, String ownerId);

    @Query(value = """
            WITH RECURSIVE material_path AS (
                -- Base case: start with the target material
                SELECT 
                    material_id,
                    parent_material_id,
                    name,
                    material_type,
                    0 as depth
                FROM material
                WHERE material_id = :materialId
                  AND passive_id IS NULL
            
                UNION ALL
            
                -- Recursive case: get parent materials
                SELECT 
                    m.material_id,
                    m.parent_material_id,
                    m.name,
                    m.material_type,
                    mp.depth + 1 as depth
                FROM material m
                INNER JOIN material_path mp ON m.material_id = mp.parent_material_id
                WHERE m.passive_id IS NULL
            )
            SELECT * FROM material_path
            ORDER BY depth DESC
            """, nativeQuery = true)
    List<Object[]> findMaterialPath(@Param("materialId") String materialId);

    Page<Material> findAllByMaterialTypeAndPassiveIdIsNotNullAndMedia_PassiveIdIsNull(MaterialType materialType, Pageable pageable);
}
