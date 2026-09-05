package co.jinear.core.service.mcp.tool;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolAnnotations;
import co.jinear.core.model.mcp.McpToolDefinition;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpToolRegistry {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{1,64}$");
    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private final List<McpTool> tools;
    private final Map<String, McpTool> byName = new LinkedHashMap<>();

    @PostConstruct
    void index() {
        tools.stream()
                .sorted(Comparator.comparing(McpTool::name))
                .forEach(tool -> {
                    validate(tool.definition());
                    if (byName.containsKey(tool.name())) {
                        throw new IllegalStateException("Duplicate MCP tool name: " + tool.name());
                    }
                    byName.put(tool.name(), tool);
                });
        log.info("[MCP] Registered {} tools.", byName.size());
    }

    public Optional<McpTool> find(String name) {
        return Optional.ofNullable(byName.get(name));
    }

    public List<McpTool> all() {
        return List.copyOf(byName.values());
    }

    public ArrayNode toolsArray() {
        ArrayNode array = FACTORY.arrayNode();
        byName.values().forEach(tool -> array.add(describe(tool.definition())));
        return array;
    }

    public ObjectNode describe(McpToolDefinition definition) {
        ObjectNode node = FACTORY.objectNode();
        node.put("name", definition.getName());
        node.put("title", definition.title());
        node.put("description", definition.getDescription());
        node.set("inputSchema", definition.getInputSchema());
        if (definition.getOutputSchema() != null) {
            node.set("outputSchema", definition.getOutputSchema());
        }
        McpToolAnnotations annotations = definition.getAnnotations();
        ObjectNode annotationNode = node.putObject("annotations");
        annotationNode.put("title", annotations.getTitle());
        annotationNode.put("readOnlyHint", annotations.isReadOnlyHint());
        annotationNode.put("destructiveHint", annotations.isDestructiveHint());
        annotationNode.put("idempotentHint", annotations.isIdempotentHint());
        annotationNode.put("openWorldHint", annotations.isOpenWorldHint());
        return node;
    }

    private void validate(McpToolDefinition definition) {
        String name = definition.getName();
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalStateException("MCP tool name must match " + NAME_PATTERN.pattern() + ": " + name);
        }
        if (definition.getAnnotations() == null || isBlank(definition.getAnnotations().getTitle())) {
            throw new IllegalStateException("MCP tool " + name + " is missing a title annotation.");
        }
        if (isBlank(definition.getDescription())) {
            throw new IllegalStateException("MCP tool " + name + " is missing a description.");
        }
        if (definition.getInputSchema() == null || !"object".equals(definition.getInputSchema().path("type").asText())) {
            throw new IllegalStateException("MCP tool " + name + " must declare an object inputSchema.");
        }
        McpToolAnnotations annotations = definition.getAnnotations();
        if (annotations.isReadOnlyHint() && annotations.isDestructiveHint()) {
            throw new IllegalStateException("MCP tool " + name + " cannot be both read only and destructive.");
        }
        if (annotations.isReadOnlyHint() && declaresWriteScope(definition)) {
            throw new IllegalStateException("MCP tool " + name + " is marked read only but requires a write scope.");
        }
    }

    private boolean declaresWriteScope(McpToolDefinition definition) {
        return definition.getRequiredScopes().stream()
                .map(OauthScope::getValue)
                .anyMatch(scope -> scope.endsWith(":write"));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
