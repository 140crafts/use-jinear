package co.jinear.core.model.entity.messaging;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_channel_info")
public class ChannelInfo {

    @Id
    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "last_channel_activity")
    private ZonedDateTime lastChannelActivity;

    @OneToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;
}
