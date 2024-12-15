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
    TEAM_WORKFLOW_STATUS("team-workflow-status", 10, TimeUnit.SECONDS),
    ACCOUNT_PASSWORD_RESET("account:password-reset", 10, TimeUnit.SECONDS),
    TASK_BOARD_EDIT("task-board:edit", 5, TimeUnit.SECONDS),
    REMINDER_JOB_PROCESS("reminder-job:process", 15, TimeUnit.MINUTES),
    CONVERSATION_INIT("conversation-init", 20, TimeUnit.SECONDS),
    CONVERSATION("conversation", 20, TimeUnit.SECONDS),
    PROJECT_MILESTONE("project:milestone", 20, TimeUnit.SECONDS),
    PROJECT_DOMAIN("project:domain", 10, TimeUnit.SECONDS);

    private final String key;
    private final int ttl;
    private final TimeUnit timeUnit;
}
