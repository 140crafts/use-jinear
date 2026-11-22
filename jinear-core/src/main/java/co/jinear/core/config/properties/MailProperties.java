package co.jinear.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@PropertySource("classpath:application.properties")
public class MailProperties {

    private static final List<String> PLACEHOLDER_MAIL_HOSTS = List.of("localhost", "smtp.example.com");
    private static final String PLACEHOLDER_MAIL_USERNAME = "change-me";

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.username}")
    private String mailUserName;

    @Value("${spring.mail.sender-address}")
    private String senderAddress;

    @Value("${jinear.mail.app-name:Jinear}")
    private String appName;

    @Value("${jinear.mail.logo-url:https://jinear.co/images/icon/icon-96x96-mail.png}")
    private String logoUrl;

    @Value("${jinear.mail.footer-url:https://jinear.co}")
    private String footerUrl;

    @Value("${jinear.mail.footer-text:jinear.co}")
    private String footerText;

    public boolean isMailConfigured() {
        boolean hostConfigured = StringUtils.isNotBlank(mailHost) && !PLACEHOLDER_MAIL_HOSTS.contains(mailHost.toLowerCase());
        boolean usernameIsPlaceholder = PLACEHOLDER_MAIL_USERNAME.equalsIgnoreCase(mailUserName);
        return hostConfigured && !usernameIsPlaceholder;
    }
}
