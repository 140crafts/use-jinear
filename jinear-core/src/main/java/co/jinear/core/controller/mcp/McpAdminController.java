package co.jinear.core.controller.mcp;

import co.jinear.core.manager.mcp.McpAdminManager;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "v1/admin/mcp")
@RequiredArgsConstructor
public class McpAdminController {

    private final McpAdminManager mcpAdminManager;

    @GetMapping("/analytics")
    public McpAnalyticsResponse analytics() {
        return mcpAdminManager.analytics();
    }

    @GetMapping("/log/list")
    public McpToolCallLogListingResponse listLogs(@RequestParam(defaultValue = "0") Integer page) {
        return mcpAdminManager.listLogs(page);
    }
}
