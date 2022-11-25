package co.jinear.core.model.enumtype.workspace;

import lombok.Getter;

@Getter
public enum WorkspaceActivityType {
    JOIN,
    LEAVE,
    KICKED_OUT,
    REQUESTED_ACCESS,
    PLACED_BET;
}
