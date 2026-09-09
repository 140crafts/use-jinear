package co.jinear.core.model.mcp.input;

import lombok.experimental.UtilityClass;

@UtilityClass
public class McpInputDescriptions {

    public static final String WORKSPACE_ID = "Workspace id, from list_workspaces.";
    public static final String TEAM_ID = "Team id, from list_teams.";
    public static final String TASK_ID = "Task id.";
    public static final String PAGE = "Zero based page number. Defaults to 0.";
    public static final String PAGE_SIZE = "Items per page. Defaults to 20.";
}
