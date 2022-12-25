package co.jinear.core.model.enumtype.workspace;

import lombok.Getter;

@Getter
public enum WorkspaceActivityType {
    MEMBER_JOIN,
    MEMBER_LEFT,
    MEMBER_REMOVED,
    MEMBER_REQUESTED_ACCESS,
    //
    TASK_INITIALIZED,
    TASK_CLOSED,
    EDIT_TASK_TITLE,
    EDIT_TASK_DESC,
    TASK_UPDATE_WORKFLOW_STATUS,
    TASK_CHANGE_ASSIGNEE,
    TASK_CHANGE_ASSIGNED_DATE,
    TASK_CHANGE_DUE_DATE;
}
