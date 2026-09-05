package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.service.mcp.McpDiscoveryService;
import co.jinear.core.controller.mcp.McpController;
import co.jinear.core.model.enumtype.account.RoleType;
import co.jinear.core.model.vo.oauth.OauthAccessTokenVo;
import co.jinear.core.service.mcp.McpProtocolService;
import co.jinear.core.service.mcp.McpToolCallLogService;
import co.jinear.core.service.mcp.tool.McpTool;
import co.jinear.core.service.mcp.tool.McpToolRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The transport contract.
 * <p>
 * The refusal shape is the part that matters most. A client only offers the user a
 * connect prompt when the HTTP request itself fails with 401 and a WWW-Authenticate
 * header; a 200 carrying a tool error reads as a tool that failed, and the user is never
 * asked to sign in.
 */
class McpControllerTest {

    private static final String RESOURCE_METADATA =
            "https://api.jinear.test/.well-known/oauth-protected-resource/mcp";

    private MockMvc mockMvc;
    private McpProperties properties;

    @BeforeEach
    void setUp() {
        properties = new McpProperties();
        properties.setEnabled(Boolean.TRUE);
        properties.setResourceUrl("https://api.jinear.test/mcp");

        List<McpTool> tools = List.of(
                McpTestTools.publicTool(),
                McpTestTools.readTool(),
                McpTestTools.writeTool(),
                McpTestTools.throwingTool(),
                McpTestTools.explodingTool());
        McpToolRegistry registry = new McpToolRegistry(tools);
        ReflectionTestUtils.invokeMethod(registry, "index");

        McpToolCallLogService logService = Mockito.mock(McpToolCallLogService.class);
        McpProtocolService protocolService = new McpProtocolService(registry, logService, new ObjectMapper());
        OauthProperties oauthProperties = new OauthProperties();
        oauthProperties.setIssuerUrl("https://api.jinear.test");
        McpDiscoveryService discoveryService = new McpDiscoveryService(properties, oauthProperties);
        McpController controller = new McpController(protocolService, registry, logService, properties, discoveryService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // --- handshake -------------------------------------------------------------

    @Test
    void initializeEchoesTheClientsProtocolVersionWhenWeSpeakIt() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":1,"method":"initialize",
                         "params":{"protocolVersion":"2025-06-18","capabilities":{},"clientInfo":{"name":"test","version":"1"}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.protocolVersion").value("2025-06-18"))
                .andExpect(jsonPath("$.result.serverInfo.name").value("jinear"))
                .andExpect(jsonPath("$.result.capabilities.tools").exists())
                .andExpect(jsonPath("$.result.instructions").isNotEmpty());
    }

    @Test
    void initializeFallsBackToOurPreferredVersionForAnUnknownOne() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"1999-01-01"}}
                        """))
                .andExpect(jsonPath("$.result.protocolVersion").value(McpProtocolService.PREFERRED_PROTOCOL_VERSION));
    }

    @Test
    void serverInstructionsStayInsideTheFiveHundredAndTwelveCharacterLimit() throws Exception {
        String instructions = (String) ReflectionTestUtils.getField(McpProtocolService.class, "INSTRUCTIONS");
        org.assertj.core.api.Assertions.assertThat(instructions).isNotNull();
        org.assertj.core.api.Assertions.assertThat(instructions.length()).isLessThanOrEqualTo(512);
    }

    @Test
    void pingAnswersEmptily() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"id\":7,\"method\":\"ping\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.result").isMap());
    }

    // --- catalog ---------------------------------------------------------------

    @Test
    void toolsListWorksWithoutAnyCredential() throws Exception {
        // A reviewer, and any client before sign in, must be able to read the whole catalog.
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.tools").isArray())
                .andExpect(jsonPath("$.result.tools[0].title").isNotEmpty())
                .andExpect(jsonPath("$.result.tools[0].annotations.readOnlyHint").exists());
    }

    @Test
    void aToolWithNoScopesRunsWithoutACredential() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"public_ping","arguments":{}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.isError").value(false));
    }

    // --- refusals --------------------------------------------------------------

    @Test
    void anUnauthenticatedProtectedCallFailsTheHttpRequestWithAChallenge() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"read_something","arguments":{}}}
                        """))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("error=\"invalid_token\"")))
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("resource_metadata=\"" + RESOURCE_METADATA + "\"")))
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("scope=\"tasks:read\"")));
    }

    @Test
    void aTokenMissingAScopeGetsAStepUpChallengeThatKeepsWhatWasAlreadyGranted() throws Exception {
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":5,"method":"tools/call","params":{"name":"write_something","arguments":{"title":"x"}}}
                        """))
                .andExpect(status().isForbidden())
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("error=\"insufficient_scope\"")))
                // Both the granted and the newly required scope, so re-consent does not
                // silently drop a permission the client was already relying on.
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("tasks:read")))
                .andExpect(header().string("WWW-Authenticate",
                        org.hamcrest.Matchers.containsString("tasks:write")));
    }

    @Test
    void aTokenWithTheScopeGetsThrough() throws Exception {
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":6,"method":"tools/call","params":{"name":"read_something","arguments":{}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.isError").value(false))
                .andExpect(jsonPath("$.result.structuredContent.accountId").value("account-1"));
    }

    // --- errors ----------------------------------------------------------------

    @Test
    void anUnknownMethodIsAProtocolError() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"id\":8,\"method\":\"resources/list\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.code").value(McpProtocolService.ERROR_METHOD_NOT_FOUND));
    }

    @Test
    void anUnknownToolIsAProtocolError() throws Exception {
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":9,"method":"tools/call","params":{"name":"no_such_tool","arguments":{}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error.code").value(McpProtocolService.ERROR_INVALID_PARAMS));
    }

    @Test
    void badArgumentsAreAToolErrorSoTheModelCanCorrectThem() throws Exception {
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":10,"method":"tools/call","params":{"name":"bad_arguments","arguments":{}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.result.isError").value(true))
                .andExpect(jsonPath("$.result.content[0].text",
                        org.hamcrest.Matchers.containsString("must look like a ULID")));
    }

    @Test
    void anUnexpectedFailureStillReturnsSomethingActionable() throws Exception {
        authenticateWith(Set.of("tasks:read"));

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        {"jsonrpc":"2.0","id":11,"method":"tools/call","params":{"name":"explodes","arguments":{}}}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.isError").value(true))
                // Never the raw exception, and never a bare "Internal Server Error".
                .andExpect(jsonPath("$.result.content[0].text",
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("database is on fire"))))
                .andExpect(jsonPath("$.result.content[0].text",
                        org.hamcrest.Matchers.containsString("could not be completed")));
    }

    // --- batching and notifications --------------------------------------------

    @Test
    void aBatchAnswersWithAnArray() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        [{"jsonrpc":"2.0","id":1,"method":"ping"},
                         {"jsonrpc":"2.0","id":2,"method":"tools/list"}]
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].result.tools").isArray());
    }

    @Test
    void aNotificationGetsNoResponseBody() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"method\":\"notifications/initialized\"}"))
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));
    }

    @Test
    void aBatchIsRefusedAsAWholeWhenAnyCallInItNeedsAuthorization() throws Exception {
        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON).content("""
                        [{"jsonrpc":"2.0","id":1,"method":"ping"},
                         {"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"read_something","arguments":{}}}]
                        """))
                .andExpect(status().isUnauthorized());
    }

    // --- other verbs -----------------------------------------------------------

    @Test
    void getIsRefusedBecauseThereIsNoServerInitiatedStream() throws Exception {
        mockMvc.perform(get("/mcp"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", org.hamcrest.Matchers.containsString("POST")));
    }

    @Test
    void deleteEndsTheNonExistentSessionQuietly() throws Exception {
        mockMvc.perform(delete("/mcp")).andExpect(status().isNoContent());
    }

    @Test
    void theWholeEndpointDisappearsWhenTheFeatureIsOff() throws Exception {
        properties.setEnabled(Boolean.FALSE);

        mockMvc.perform(post("/mcp").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"tools/list\"}"))
                .andExpect(status().isNotFound());
    }

    private void authenticateWith(Set<String> scopes) {
        OauthAccessTokenVo vo = new OauthAccessTokenVo();
        vo.setAccountId("account-1");
        vo.setConnectionId("connection-1");
        vo.setClientId("https://claude.ai/client.json");
        vo.setScopes(scopes);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "account-1", null, List.of(new SimpleGrantedAuthority(RoleType.USER.getAuthority())));
        authentication.setDetails(vo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
