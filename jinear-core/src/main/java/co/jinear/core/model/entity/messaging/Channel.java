package co.jinear.core.model.entity.messaging;

import co.jinear.core.converter.messaging.ChannelParticipationTypeConverter;
import co.jinear.core.converter.messaging.ChannelVisibilityTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.messaging.ChannelParticipationType;
import co.jinear.core.model.enumtype.messaging.ChannelVisibilityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class Channel extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "title")
    private String title;

    @Convert(converter = ChannelParticipationTypeConverter.class)
    @Column(name = "participation_type")
    private ChannelParticipationType participationType;

    @Convert(converter = ChannelVisibilityTypeConverter.class)
    @Column(name = "channel_visibility_type")
    private ChannelVisibilityType channelVisibilityType;

    @OneToMany(mappedBy = "channel")
    @Where(clause = "passive_id is null")
    private Set<ChannelSettings> settings;

    @OneToMany(mappedBy = "channel")
    @Where(clause = "passive_id is null")
    @OrderBy("createdDate ASC")
    private Set<ChannelMember> members;
}
