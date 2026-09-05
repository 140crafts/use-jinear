package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class McpProperties {

    @Value("${jinear.mcp.enabled:false}")
    private Boolean enabled = Boolean.FALSE;

    @Value("${jinear.mcp.resource-url}")
    private String resourceUrl;

    @Value("${jinear.mcp.documentation-url:https://jinear.co/mcp/}")
    private String documentationUrl;

    @Value("${jinear.mcp.log-retention-days:30}")
    private Integer logRetentionDays = 30;

    @Value("${jinear.mcp.max-page-size:50}")
    private Integer maxPageSize = 50;
}
