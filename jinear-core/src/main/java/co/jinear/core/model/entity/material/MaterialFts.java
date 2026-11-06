package co.jinear.core.model.entity.material;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "mv_material_fts")
public class MaterialFts {

    @Id
    @Column(name = "material_id")
    private String materialId;

    @Column(name = "workspace_id")
    private String workspaceId;
}
