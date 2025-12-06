package co.jinear.core.model.entity.material;

import co.jinear.core.converter.material.MaterialTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.material.MaterialType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "material")
public class Material extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "material_id")
    private String materialId;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "parent_material_id")
    private String parentMaterialId;

    @Column(name = "media_id")
    private String mediaId;

    @Convert(converter = MaterialTypeConverter.class)
    @Column(name = "material_type")
    private MaterialType materialType;

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "color")
    private String color;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private Media media;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "parent_material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Material parent;

    @OneToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "parent_material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Set<Material> children;
}
