package co.jinear.core.model.enumtype.workspace;

import lombok.Getter;

@Getter
public enum WorkspaceJoinType {
    NEVER,
    PUBLIC,
    WITH_REQUEST,
    WITH_PASSWORD;
}
