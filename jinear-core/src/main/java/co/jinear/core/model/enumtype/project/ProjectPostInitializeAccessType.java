package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectPostInitializeAccessType {

    WORKSPACE_ADMINS(0),
    PROJECT_LEAD(1),
    PROJECT_TEAM_ADMINS(2),
    PROJECT_TEAM_MEMBERS(3),
    WORKSPACE_MEMBERS(4);

    private final int value;
}
