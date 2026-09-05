package co.jinear.core.oauth;

import co.jinear.core.controller.oauth.provider.OauthTokenController;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthTokenManager;
import co.jinear.core.service.oauth.provider.OauthErrorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The token endpoint's wire contract.
 * <p>
 * Two content types, and they are not interchangeable. RFC 6749 makes the token endpoint
 * form encoded and RFC 7591 makes registration JSON, so a framework configured for one
 * body parser answers the other with a 415 during the very first connection attempt.
 */
class OauthTokenControllerTest {

    private OauthTokenManager tokenManager;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tokenManager = Mockito.mock(OauthTokenManager.class);
        OauthTokenController controller = new OauthTokenController(tokenManager, new OauthErrorMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void acceptsAFormEncodedTokenRequest() throws Exception {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("access_token", "token-value");
        body.put("token_type", "Bearer");
        body.put("expires_in", 3600L);
        body.put("refresh_token", "refresh-value");
        body.put("scope", "tasks:read");
        Mockito.when(tokenManager.token(Mockito.anyMap())).thenReturn(body);

        mockMvc.perform(post("/v1/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "authorization_code")
                        .param("code", "row-1.secret")
                        .param("redirect_uri", "https://claude.ai/api/mcp/auth_callback")
                        .param("code_verifier", "v".repeat(64)))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.access_token").value("token-value"))
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.refresh_token").value("refresh-value"));
    }

    @Test
    void reportsADeadGrantAsInvalidGrant() throws Exception {
        // Claude only treats a refresh failure as "this connection is gone, start over"
        // when the code is exactly invalid_grant.
        Mockito.when(tokenManager.token(Mockito.anyMap()))
                .thenThrow(new BusinessException("oauth.error.invalid-grant"));

        mockMvc.perform(post("/v1/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "refresh_token")
                        .param("refresh_token", "row-1.spent"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_grant"));
    }

    @Test
    void reportsAnUnknownClientAsInvalidClientWithA401() throws Exception {
        Mockito.when(tokenManager.token(Mockito.anyMap()))
                .thenThrow(new BusinessException("oauth.error.invalid-client"));

        mockMvc.perform(post("/v1/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "authorization_code"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("invalid_client"));
    }

    @Test
    void acceptsAJsonDynamicRegistration() throws Exception {
        Map<String, Object> registered = new LinkedHashMap<>();
        registered.put("client_id", "01hs0000000000000000000000");
        registered.put("token_endpoint_auth_method", "none");
        registered.put("redirect_uris", List.of("https://claude.ai/api/mcp/auth_callback"));
        Mockito.when(tokenManager.register(Mockito.any())).thenReturn(registered);

        mockMvc.perform(post("/v1/oauth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"client_name":"Claude","redirect_uris":["https://claude.ai/api/mcp/auth_callback"],
                                 "grant_types":["authorization_code","refresh_token"],"token_endpoint_auth_method":"none"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client_id").isNotEmpty())
                // A public client gets no secret; PKCE is what protects the exchange.
                .andExpect(jsonPath("$.client_secret").doesNotExist())
                .andExpect(jsonPath("$.token_endpoint_auth_method").value("none"));
    }

    @Test
    void revocationAlwaysAnswersOk() throws Exception {
        // RFC 7009 requires a 200 whether or not the token was real, so a caller cannot
        // probe which tokens exist.
        mockMvc.perform(post("/v1/oauth/revoke")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "definitely-not-a-token"))
                .andExpect(status().isOk());
    }
}
