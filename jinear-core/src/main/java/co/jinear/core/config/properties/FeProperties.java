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
public class FeProperties {

    @Value("${fe.email-confirmation-url}")
    private String emailConfirmationUrl;

    @Value("${fe.password-reset-url}")
    private String passwordResetUrl;

    @Value("${fe.task-url}")
    private String taskUrl;

    @Value("${fe.workspace-invitation-url}")
    private String workspaceInvitationUrl;

    @Value("${fe.home-url}")
    private String homeUrl;
}
