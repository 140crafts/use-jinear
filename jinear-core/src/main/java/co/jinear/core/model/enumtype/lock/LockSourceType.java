package co.jinear.core.model.enumtype.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum LockSourceType {
    BALANCE("balance", 1, TimeUnit.HOURS),
    TOPIC_TASK_INIT("topic-task-init", 10, TimeUnit.SECONDS),
    TEAM_TASK_INIT("team-task-init", 10, TimeUnit.SECONDS),
    TEAM_WORKFLOW_STATUS("team-workflow-status", 10, TimeUnit.SECONDS);

    private String key;
    private int ttl;
    private TimeUnit timeUnit;
}
