package co.jinear.core.oauth;

import co.jinear.core.controller.advice.OauthApiAdvice;
import co.jinear.core.controller.advice.OauthCacheHeaderAdvice;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.controller.oauth.provider.OauthTokenController;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.manager.oauth.provider.OauthTokenManager;
import co.jinear.core.model.response.oauth.OauthClientRegistrationResponse;
import co.jinear.core.model.response.oauth.OauthTokenResponse;
import co.jinear.core.service.oauth.provider.OauthErrorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OauthTokenControllerTest {

    private OauthTokenManager tokenManager;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tokenManager = Mockito.mock(OauthTokenManager.class);
        OauthTokenController controller = new OauthTokenController(tokenManager);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new OauthApiAdvice(new OauthErrorMapper()),
                        new OauthCacheHeaderAdvice(new OauthProperties()))
                .build();
    }

    @Test
    void acceptsAFormEncodedTokenRequest() throws Exception {
        OauthTokenResponse body = new OauthTokenResponse();
        body.setAccessToken("token-value");
        body.setTokenType("Bearer");
        body.setExpiresIn(3600L);
        body.setRefreshToken("refresh-value");
        body.setScope("tasks:read");
        Mockito.when(tokenManager.token(Mockito.any())).thenReturn(body);

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
    void bindsTheSnakeCaseFormNamesOntoTheRequest() throws Exception {
        Mockito.when(tokenManager.token(Mockito.any())).thenReturn(new OauthTokenResponse());

        mockMvc.perform(post("/v1/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "authorization_code")
                        .param("code", "row-1.secret")
                        .param("redirect_uri", "https://claude.ai/api/mcp/auth_callback")
                        .param("client_id", "client-1")
                        .param("code_verifier", "v".repeat(64)))
                .andExpect(status().isOk());

        Mockito.verify(tokenManager).token(Mockito.argThat(request ->
                "authorization_code".equals(request.getGrantType())
                        && "row-1.secret".equals(request.getCode())
                        && "https://claude.ai/api/mcp/auth_callback".equals(request.getRedirectUri())
                        && "client-1".equals(request.getClientId())
                        && "v".repeat(64).equals(request.getCodeVerifier())));
    }

    @Test
    void reportsADeadGrantAsInvalidGrant() throws Exception {
        Mockito.when(tokenManager.token(Mockito.any()))
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
        Mockito.when(tokenManager.token(Mockito.any()))
                .thenThrow(new BusinessException("oauth.error.invalid-client"));

        mockMvc.perform(post("/v1/oauth/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "authorization_code"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("invalid_client"));
    }

    @Test
    void acceptsAJsonDynamicRegistration() throws Exception {
        OauthClientRegistrationResponse registered = new OauthClientRegistrationResponse();
        registered.setClientId("01hs0000000000000000000000");
        registered.setTokenEndpointAuthMethod("none");
        registered.setRedirectUris(List.of("https://claude.ai/api/mcp/auth_callback"));
        Mockito.when(tokenManager.register(Mockito.any())).thenReturn(registered);

        mockMvc.perform(post("/v1/oauth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"client_name":"Claude","redirect_uris":["https://claude.ai/api/mcp/auth_callback"],
                                 "grant_types":["authorization_code","refresh_token"],"token_endpoint_auth_method":"none"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client_id").isNotEmpty())
                .andExpect(jsonPath("$.client_secret").doesNotExist())
                .andExpect(jsonPath("$.token_endpoint_auth_method").value("none"));
    }

    @Test
    void readsTheSnakeCaseRegistrationBody() throws Exception {
        Mockito.when(tokenManager.register(Mockito.any())).thenReturn(new OauthClientRegistrationResponse());

        mockMvc.perform(post("/v1/oauth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"client_name":"Claude","redirect_uris":["https://claude.ai/api/mcp/auth_callback"],
                                 "grant_types":["authorization_code"],"token_endpoint_auth_method":"none",
                                 "unknown_member":"ignored"}
                                """))
                .andExpect(status().isCreated());

        Mockito.verify(tokenManager).register(Mockito.argThat(request ->
                "Claude".equals(request.getClientName())
                        && List.of("https://claude.ai/api/mcp/auth_callback").equals(request.getRedirectUris())
                        && List.of("authorization_code").equals(request.getGrantTypes())
                        && "none".equals(request.getTokenEndpointAuthMethod())));
    }

    @Test
    void revocationAlwaysAnswersOk() throws Exception {
        mockMvc.perform(post("/v1/oauth/revoke")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("token", "definitely-not-a-token"))
                .andExpect(status().isOk());
    }
}
