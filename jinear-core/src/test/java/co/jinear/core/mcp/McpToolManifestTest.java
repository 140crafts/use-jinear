package co.jinear.core.mcp;

import co.jinear.core.config.jackson.PublishedDocumentJsonConfiguration;
import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.exception.mcp.McpDisabledException;
import co.jinear.core.manager.mcp.tool.McpToolDefinitionBuilder;
import co.jinear.core.manager.mcp.tool.McpToolRegistry;
import co.jinear.core.model.enumtype.oauth.OauthScope;
import co.jinear.core.model.mcp.McpToolResult;
import co.jinear.core.model.mcp.schema.McpSchemaGenerator;
import co.jinear.core.model.response.mcp.McpServerInfoResponse;
import co.jinear.core.model.response.mcp.McpToolManifestResponse;
import co.jinear.core.validator.mcp.McpEnabledValidator;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

class McpToolManifestTest {

    @Test
    void keysTheScopesByToolInCatalogOrder() {
        McpToolRegistry registry = new McpToolRegistry(List.of(
                McpTestTools.writeTool(), McpTestTools.publicTool(), McpTestTools.readTool()));

        assertThat(registry.requiredScopes()).containsExactly(
                entry("public_ping", List.of()),
                entry("read_something", List.of("tasks:read")),
                entry("write_something", List.of("tasks:write")));
    }

    @Test
    void sortsEachToolsScopesSoTheManifestIsStableAcrossRuns() {
        McpToolRegistry registry = new McpToolRegistry(List.of(new McpStubTool(
                McpToolDefinitionBuilder
                        .named("read_and_write")
                        .title("Read and write")
                        .description("Reads and writes a record. Requires both task scopes.")
                        .input(McpSchemaGenerator.forInput(McpStubInputs.NoArguments.class))
                        .write()
                        .scopes(OauthScope.TASKS_WRITE, OauthScope.TASKS_READ)
                        .build(),
                (context, args) -> McpToolResult.of(McpTestPayload.result("done")))));

        assertThat(registry.requiredScopes().get("read_and_write")).containsExactly("tasks:read", "tasks:write");
    }

    @Test
    void refusesWhenMcpIsOff() {
        McpProperties properties = new McpProperties();
        properties.setEnabled(Boolean.FALSE);

        assertThatThrownBy(() -> new McpEnabledValidator(properties).validateMcpIsEnabled())
                .isInstanceOf(McpDisabledException.class);
    }

    @Test
    void prettyPrintsTheManifestAndLeavesOtherResponsesCompact() throws IOException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        new PublishedDocumentJsonConfiguration().extendMessageConverters(List.of(converter));

        McpToolManifestResponse manifest = new McpToolManifestResponse();
        manifest.setTools(List.of());
        manifest.setScopes(Map.of());

        assertThat(write(converter, manifest)).isEqualToNormalizingNewlines("{\n  \"tools\" : [ ],\n  \"scopes\" : { }\n}");
        assertThat(write(converter, new McpServerInfoResponse())).doesNotContain("\n");
    }

    private String write(MappingJackson2HttpMessageConverter converter, Object body) throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        converter.write(body, MediaType.APPLICATION_JSON, message);
        return message.getBodyAsString();
    }
}
