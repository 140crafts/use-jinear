package co.jinear.core.mcp;

import co.jinear.core.config.properties.McpProperties;
import co.jinear.core.converter.mcp.McpDtoConverter;
import co.jinear.core.manager.mcp.McpManagementManager;
import co.jinear.core.model.dto.mcp.McpServerInfoDto;
import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.repository.mcp.McpToolCallLogRepository;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.management.InstanceFlagService;
import co.jinear.core.service.mcp.analytics.McpAnalyticsService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * What a member is told about their instance's MCP server.
 * <p>
 * Two switches decide this: the property, which says whether the server exists at all,
 * and the instance flag, which is the administrator's. Reporting either one alone would
 * hand a member a server address that cannot be connected, and the resulting failure
 * happens inside Claude where there is nothing to read.
 */
class McpServerInfoTest {

    private InstanceFlagService instanceFlagService;
    private McpProperties properties;
    private McpManagementManager manager;

    @BeforeEach
    void setUp() {
        instanceFlagService = Mockito.mock(InstanceFlagService.class);
        properties = new McpProperties();
        properties.setResourceUrl("https://api.jinear.test/mcp");
        properties.setDocumentationUrl("https://jinear.test/mcp/");

        manager = new McpManagementManager(
                Mockito.mock(McpToolCallLogRepository.class),
                Mockito.mock(McpAnalyticsService.class),
                Mockito.mock(McpDtoConverter.class),
                Mockito.mock(SessionInfoService.class),
                Mockito.mock(WorkspaceValidator.class),
                instanceFlagService,
                properties);
    }

    private McpServerInfoDto infoWith(boolean propertyEnabled, boolean flagEnabled) {
        properties.setEnabled(propertyEnabled);
        Mockito.when(instanceFlagService.isEnabled(InstanceFlagType.MCP_SERVER)).thenReturn(flagEnabled);
        return manager.retrieveServerInfo().getMcpServerInfoDto();
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

    /**
     * The documentation link is useful precisely when the feature is off, because that is
     * when somebody wants to know how to turn it on.
     */
    @Test
    void alwaysReportsTheDocumentationUrl() {
        assertThat(infoWith(false, false).getDocumentationUrl()).isEqualTo("https://jinear.test/mcp/");
        assertThat(infoWith(true, true).getDocumentationUrl()).isEqualTo("https://jinear.test/mcp/");
    }
}
