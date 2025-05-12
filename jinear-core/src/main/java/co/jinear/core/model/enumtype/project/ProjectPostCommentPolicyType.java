package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectPostCommentPolicyType {

    WORKSPACE_ADMINS(0),
    PROJECT_LEAD(1),
    PROJECT_TEAM_ADMINS(2),
    PROJECT_TEAM_MEMBERS(3),
    WORKSPACE_MEMBERS(4),
    ANY_LOGGED_IN_USER(5);

    private final int value;
}
