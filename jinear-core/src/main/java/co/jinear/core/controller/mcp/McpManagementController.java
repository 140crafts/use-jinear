package co.jinear.core.controller.mcp;

import co.jinear.core.manager.mcp.McpManagementManager;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpServerInfoResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "v1/mcp")
@RequiredArgsConstructor
public class McpManagementController {

    private final McpManagementManager mcpManagementManager;

    @GetMapping("/info")
    public McpServerInfoResponse retrieveServerInfo() {
        return mcpManagementManager.retrieveServerInfo();
    }

    @GetMapping("/log/list/workspace/{workspaceId}")
    public McpToolCallLogListingResponse listWorkspaceLogs(@PathVariable String workspaceId,
                                                           @RequestParam(defaultValue = "0") Integer page) {
        return mcpManagementManager.listWorkspaceLogs(workspaceId, page);
    }

    @GetMapping("/analytics/workspace/{workspaceId}")
    public McpAnalyticsResponse workspaceAnalytics(@PathVariable String workspaceId) {
        return mcpManagementManager.workspaceAnalytics(workspaceId);
    }
}
