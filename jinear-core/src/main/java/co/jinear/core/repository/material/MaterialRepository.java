package co.jinear.core.repository.material;

import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, String> {

    Optional<Material> findByMaterialIdAndPassiveIdIsNull(String materialId);

    boolean existsByMaterialIdAndMaterialTypeAndWorkspaceIdAndPassiveIdIsNull(String materialId, MaterialType materialType, String workspaceId);

    boolean existsByWorkspaceIdAndMaterialIdAndOwnerIdAndPassiveIdIsNull(String workspaceId, String materialId, String ownerId);
}
