package co.jinear.core.model.entity.chat;

import co.jinear.core.converter.chat.ChannelSettingTypeConverter;
import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.enumtype.chat.ChannelSettingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "channel_settings")
public class ChannelSettings extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "channel_settings_id")
    private String channelSettingsId;

    @Column(name = "channel_id")
    private String channelId;

    @Convert(converter = ChannelSettingTypeConverter.class)
    @Column(name = "settings_type")
    private ChannelSettingType settingsType;

    @Column(name = "settings_value")
    private String settingsValue;

    @ManyToOne
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;
}
