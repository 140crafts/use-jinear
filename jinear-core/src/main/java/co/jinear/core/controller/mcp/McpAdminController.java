package co.jinear.core.controller.mcp;

import co.jinear.core.manager.mcp.McpAdminManager;
import co.jinear.core.model.response.mcp.McpAnalyticsResponse;
import co.jinear.core.model.response.mcp.McpToolCallLogListingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping(value = "v1/admin/mcp")
@RequiredArgsConstructor
public class McpAdminController {

    private final McpAdminManager mcpAdminManager;

    @GetMapping("/analytics")
    @ResponseStatus(HttpStatus.OK)
    public McpAnalyticsResponse analytics() {
        return mcpAdminManager.analytics();
    }

    @GetMapping("/log/list")
    @ResponseStatus(HttpStatus.OK)
    public McpToolCallLogListingResponse listLogs(@RequestParam(required = false, defaultValue = "0") Integer page) {
        return mcpAdminManager.listLogs(page);
    }
}
