package co.jinear.core.mcp;

import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolDefinition;
import co.jinear.core.service.mcp.tool.McpTool;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class McpToolCatalogTest {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{1,64}$");

    private static final List<String> INSTRUCTION_SHAPED = List.of(
            "ignore previous",
            "ignore all previous",
            "you must always",
            "do not call",
            "always prefer this tool",
            "instead of any other tool",
            "system prompt",
            "disregard");

    static Stream<McpTool> catalog() {
        return McpRealCatalog.tools().stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everyToolHasATitle(McpTool tool) {
        assertThat(tool.definition().title())
                .as("tool %s must carry a title annotation", tool.name())
                .isNotBlank();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everyToolNameFitsTheLimitAndCharacterSet(McpTool tool) {
        assertThat(NAME_PATTERN.matcher(tool.name()).matches())
                .as("tool name %s must match %s", tool.name(), NAME_PATTERN.pattern())
                .isTrue();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everyToolDescribesWhatItDoesAtSomeLength(McpTool tool) {
        assertThat(tool.definition().getDescription())
                .as("tool %s needs a description a model can act on", tool.name())
                .isNotBlank()
                .hasSizeGreaterThan(40);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void noDescriptionInstructsTheModel(McpTool tool) {
        String description = tool.definition().getDescription().toLowerCase(Locale.ROOT);
        INSTRUCTION_SHAPED.forEach(phrase ->
                assertThat(description)
                        .as("tool %s describes behaviour instead of function", tool.name())
                        .doesNotContain(phrase));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everyToolDeclaresAnObjectInputSchema(McpTool tool) {
        JsonNode schema = tool.definition().getInputSchema();
        assertThat(schema).as("tool %s must declare an inputSchema", tool.name()).isNotNull();
        assertThat(schema.path("type").asText()).isEqualTo("object");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everySchemaPropertyIsDescribed(McpTool tool) {
        JsonNode properties = tool.definition().getInputSchema().path("properties");
        properties.fieldNames().forEachRemaining(field ->
                assertThat(properties.path(field).path("description").asText())
                        .as("tool %s, argument %s needs a description", tool.name(), field)
                        .isNotBlank());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void aToolIsNeverBothReadOnlyAndDestructive(McpTool tool) {
        var annotations = tool.definition().getAnnotations();
        assertThat(annotations.isReadOnlyHint() && annotations.isDestructiveHint())
                .as("tool %s cannot be both", tool.name())
                .isFalse();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void aReadOnlyToolNeverAsksForAWriteScope(McpTool tool) {
        McpToolDefinition definition = tool.definition();
        if (!definition.getAnnotations().isReadOnlyHint()) {
            return;
        }
        assertThat(definition.getRequiredScopes())
                .as("read only tool %s must not require a write scope", tool.name())
                .noneMatch(scope -> scope.getValue().endsWith(":write"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("catalog")
    void everyToolIsExplicitlyClosedWorld(McpTool tool) {
        assertThat(tool.definition().getAnnotations().isOpenWorldHint())
                .as("tool %s only touches this instance", tool.name())
                .isFalse();
    }

    @Test
    void toolNamesAreUnique() {
        List<String> names = McpRealCatalog.tools().stream().map(McpTool::name).toList();
        assertThat(names).doesNotHaveDuplicates();
    }

    @Test
    void readAndWriteAreSeparateTools() {
        McpRealCatalog.tools().forEach(tool -> {
            var scopes = tool.definition().getRequiredScopes();
            boolean reads = scopes.stream().anyMatch(scope -> scope.getValue().endsWith(":read"));
            boolean writes = scopes.stream().anyMatch(scope -> scope.getValue().endsWith(":write"));
            if (reads && writes) {
                assertThat(tool.definition().getAnnotations().isReadOnlyHint())
                        .as("tool %s mixes read and write scopes", tool.name())
                        .isFalse();
            }
        });
    }

    @Test
    void everyScopeThisServerAdvertisesIsActuallyUsed() {
        var used = McpRealCatalog.tools().stream()
                .flatMap(tool -> tool.definition().getRequiredScopes().stream())
                .distinct()
                .toList();
        for (OauthScope scope : OauthScope.values()) {
            if (scope == OauthScope.OFFLINE_ACCESS) {
                continue;
            }
            assertThat(used).as("scope %s is advertised but no tool requires it", scope.getValue()).contains(scope);
        }
    }

    @Test
    void theCatalogStaysSmallEnoughForRealClients() {
        assertThat(McpRealCatalog.tools()).hasSizeLessThanOrEqualTo(36);
    }

    @Test
    void theStandardSearchAndFetchPairIsPresent() {
        List<String> names = McpRealCatalog.tools().stream().map(McpTool::name).toList();
        assertThat(names).contains("search", "fetch");
    }
}
