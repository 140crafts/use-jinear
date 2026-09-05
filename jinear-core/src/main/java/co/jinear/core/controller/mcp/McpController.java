package co.jinear.core.controller.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.config.security.OauthBearerAuthenticationFilter;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.enumtype.mcp.McpToolCallStatus;
import co.jinear.core.model.mcp.McpToolContext;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.mcp.McpProtocolService;
import co.jinear.core.service.mcp.McpToolCallLogService;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class McpController {

    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private final McpProtocolService mcpProtocolService;
    private final McpToolRegistry mcpToolRegistry;
    private final McpToolCallLogService mcpToolCallLogService;
    private final McpProperties mcpProperties;
    private final McpDiscoveryService mcpDiscoveryService;

    @PostMapping(value = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> handle(@RequestBody JsonNode body) {
        if (!Boolean.TRUE.equals(mcpProperties.getEnabled())) {
            return ResponseEntity.notFound().build();
        }

        List<JsonNode> messages = body.isArray()
                ? toList((ArrayNode) body)
                : List.of(body);

        Optional<OauthAccessTokenVo> token = OauthBearerAuthenticationFilter.currentAccessToken();
        Optional<ResponseEntity<JsonNode>> challenge = challengeFor(messages, token);
        if (challenge.isPresent()) {
            return challenge.get();
        }

        McpToolContext context = contextFor(token);
        List<ObjectNode> responses = new ArrayList<>();
        messages.forEach(message -> mcpProtocolService.handle(message, context).ifPresent(responses::add));

        if (responses.isEmpty()) {
            return ResponseEntity.accepted().build();
        }
        if (!body.isArray()) {
            return ResponseEntity.ok(responses.get(0));
        }
        ArrayNode array = FACTORY.arrayNode();
        responses.forEach(array::add);
        return ResponseEntity.ok(array);
    }

    @GetMapping("/mcp")
    public ResponseEntity<Void> noStream() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.ALLOW, "POST, DELETE")
                .build();
    }

    @DeleteMapping("/mcp")
    public ResponseEntity<Void> endSession() {
        return ResponseEntity.noContent().build();
    }

    private Optional<ResponseEntity<JsonNode>> challengeFor(List<JsonNode> messages, Optional<OauthAccessTokenVo> token) {
        for (JsonNode message : messages) {
            if (!mcpProtocolService.isToolCall(message)) {
                continue;
            }
            String toolName = mcpProtocolService.toolNameOf(message);
            Optional<McpTool> tool = Objects.isNull(toolName) ? Optional.empty() : mcpToolRegistry.find(toolName);
            if (tool.isEmpty()) {
                continue;
            }
            Set<OauthScope> required = tool.get().definition().getRequiredScopes();
            if (required.isEmpty()) {
                continue;
            }
            if (token.isEmpty()) {
                mcpToolCallLogService.recordRejection(null, null, null, toolName, McpToolCallStatus.UNAUTHORIZED);
                return Optional.of(unauthorized(required));
            }
            Set<String> granted = token.get().getScopes();
            Set<String> missing = required.stream()
                    .map(OauthScope::getValue)
                    .filter(scope -> !granted.contains(scope))
                    .collect(LinkedHashSet::new, Set::add, Set::addAll);
            if (!missing.isEmpty()) {
                mcpToolCallLogService.recordRejection(token.get().getAccountId(), token.get().getConnectionId(),
                        token.get().getClientId(), toolName, McpToolCallStatus.FORBIDDEN);
                return Optional.of(insufficientScope(granted, required));
            }
        }
        return Optional.empty();
    }

    private ResponseEntity<JsonNode> unauthorized(Set<OauthScope> required) {
        String scope = required.stream().map(OauthScope::getValue).reduce((a, b) -> a + " " + b).orElse("");
        String challenge = "Bearer error=\"invalid_token\", "
                + "error_description=\"Authentication is required for this tool.\", "
                + "resource_metadata=\"" + mcpDiscoveryService.protectedResourceMetadataUrl() + "\", "
                + "scope=\"" + scope + "\"";
        ObjectNode body = FACTORY.objectNode();
        body.put("error", "invalid_token");
        body.put("error_description", "Authentication is required for this tool.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, challenge)
                .body(body);
    }

    private ResponseEntity<JsonNode> insufficientScope(Set<String> granted, Set<OauthScope> required) {
        Set<String> union = new LinkedHashSet<>(granted);
        required.forEach(scope -> union.add(scope.getValue()));
        String scope = String.join(" ", union);
        String challenge = "Bearer error=\"insufficient_scope\", "
                + "scope=\"" + scope + "\", "
                + "resource_metadata=\"" + mcpDiscoveryService.protectedResourceMetadataUrl() + "\", "
                + "error_description=\"This tool needs a permission that was not granted.\"";
        ObjectNode body = FACTORY.objectNode();
        body.put("error", "insufficient_scope");
        body.put("error_description", "This tool needs a permission that was not granted.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header(HttpHeaders.WWW_AUTHENTICATE, challenge)
                .body(body);
    }

    private McpToolContext contextFor(Optional<OauthAccessTokenVo> token) {
        return token.map(vo -> McpToolContext.builder()
                        .accountId(vo.getAccountId())
                        .connectionId(vo.getConnectionId())
                        .clientId(vo.getClientId())
                        .scopes(vo.getScopes())
                        .build())
                .orElseGet(() -> McpToolContext.builder().scopes(Set.of()).build());
    }

    private List<JsonNode> toList(ArrayNode array) {
        List<JsonNode> messages = new ArrayList<>();
        array.forEach(messages::add);
        return messages;
    }
}
