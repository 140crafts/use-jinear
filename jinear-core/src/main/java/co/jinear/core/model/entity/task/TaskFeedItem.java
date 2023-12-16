package co.jinear.core.model.entity.task;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.feed.Feed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "task_feed_item")
public class TaskFeedItem extends BaseEntity {

    @Id
    @GeneratedValue(generator = "ULID")
    @GenericGenerator(name = "ULID", strategy = "co.jinear.core.config.idgenerator.ULIDIdGenerator")
    @Column(name = "task_feed_item_id")
    private String taskFeedItemId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "feed_id")
    private String feedId;

    @Column(name = "feed_item_id")
    private String feedItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "feed_id", insertable = false, updatable = false)
    private Feed feed;
}
