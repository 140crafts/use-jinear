package co.jinear.core.model.entity.material;

import co.jinear.core.converter.material.MaterialAccessTypeConverter;
import co.jinear.core.converter.material.MaterialTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.account.Account;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "material")
@FieldNameConstants
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

    @Convert(converter = MaterialAccessTypeConverter.class)
    @Column(name = "material_access_type")
    private MaterialAccessType materialAccessType;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private Media media;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "parent_material_id", referencedColumnName = "material_id", insertable = false, updatable = false)
    private Material parent;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id", insertable = false, updatable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "material")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<MaterialAccess> materialAccesses;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @Where(clause = "passive_id is null")
    @JoinColumn(name = "owner_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account owner;
}
