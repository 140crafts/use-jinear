package co.jinear.core.model.enumtype.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamTaskVisibilityType {

    VISIBLE_TO_ALL_TEAM_MEMBERS(0),
    OWNER_ASSIGNEE_AND_ADMINS(1);

    private final int value;
}
