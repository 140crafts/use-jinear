package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.config.properties.OauthProperties;
import co.jinear.core.model.dto.mcp.McpServerInfoDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.mcp.McpServerInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class McpServerInfoTest {

    private InstanceFlagService instanceFlagService;
    private McpProperties properties;
    private OauthProperties oauthProperties;
    private McpServerInfoService service;

    @BeforeEach
    void setUp() {
        instanceFlagService = Mockito.mock(InstanceFlagService.class);
        properties = new McpProperties();
        properties.setResourceUrl("https://api.jinear.test/mcp");
        properties.setDocumentationUrl("https://jinear.test/mcp/");
        oauthProperties = new OauthProperties();
        oauthProperties.setEnabled(Boolean.TRUE);

        service = new McpServerInfoService(properties, oauthProperties, instanceFlagService);
    }

    private McpServerInfoDto infoWith(boolean propertyEnabled, boolean flagEnabled) {
        properties.setEnabled(propertyEnabled);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(flagEnabled);
        return service.retrieveServerInfo();
    }

    @Test
    void withholdsTheAddressWhenTheAuthorizationServerIsOff() {
        oauthProperties.setEnabled(Boolean.FALSE);
        McpServerInfoDto info = infoWith(true, true);

        assertThat(info.getEnabled()).isFalse();
        assertThat(info.getServerUrl()).isNull();
    }

    @Test
    void reportsTheServerAddressWhenBothSwitchesAgree() {
        McpServerInfoDto info = infoWith(true, true);

        assertThat(info.getEnabled()).isTrue();
        assertThat(info.getServerUrl()).isEqualTo("https://api.jinear.test/mcp");
    }

    @Test
    void withholdsTheAddressWhenTheAdministratorHasNotTurnedItOn() {
        McpServerInfoDto info = infoWith(true, false);

        assertThat(info.getEnabled()).isFalse();
        assertThat(info.getServerUrl()).isNull();
    }

    @Test
    void withholdsTheAddressWhenTheServerIsNotConfigured() {
        McpServerInfoDto info = infoWith(false, true);

        assertThat(info.getEnabled()).isFalse();
        assertThat(info.getServerUrl()).isNull();
    }

    @Test
    void alwaysReportsTheDocumentationUrl() {
        assertThat(infoWith(false, false).getDocumentationUrl()).isEqualTo("https://jinear.test/mcp/");
        assertThat(infoWith(true, true).getDocumentationUrl()).isEqualTo("https://jinear.test/mcp/");
    }
}
