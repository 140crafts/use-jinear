package co.jinear.core.model.entity.topic;

import co.jinear.core.model.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "topic")
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(
            name = "ULID",
            strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "topic_id")
    private String topicId;

    @Column(name = "workspace_id")
    private String workspaceId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "color")
    private String color;

    @Column(name = "name")
    private String name;

    @Column(name = "tag")
    private String tag;
}
