package co.jinear.core.config;

import jakarta.mail.Session;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.nlab.smtp.transport.factory.SmtpConnectionFactory;
import org.nlab.smtp.transport.factory.SmtpConnectionFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class MailConfig {


    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private Integer mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public SmtpConnectionPool smtpConnectionPool() {
        GenericObjectPoolConfig<ClosableSmtpConnection> config = new GenericObjectPoolConfig<>();
        config.setTestOnBorrow(true);
        config.setMinIdle(0);
        config.setMaxIdle(8);
        config.setMaxTotal(1);
        config.setJmxEnabled(false);

        config.setMinEvictableIdleTime(Duration.ofMinutes(5));
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(10));
        config.setMaxWait(Duration.ofSeconds(10));

        SmtpConnectionFactory smtpConnectionFactory = SmtpConnectionFactoryBuilder.newSmtpBuilder()
                .session(smtpSession())
                .protocol("smtp")
                .host(mailHost)
                .port(mailPort)
                .username(mailUsername)
                .password(mailPassword)
                .build();

        return new SmtpConnectionPool(smtpConnectionFactory, config);
    }

    @Bean
    public Properties smtpProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailHost);
        properties.put("mail.smtp.port", mailPort);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("smtp.starttls.required", true);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.mime.charset","UTF-8");
        return properties;
    }

    @Bean
    public Session smtpSession() {
        return Session.getDefaultInstance(smtpProperties(), null);
    }
}
