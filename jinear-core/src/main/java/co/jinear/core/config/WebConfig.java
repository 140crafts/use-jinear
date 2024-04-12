package co.jinear.core.config;

import co.jinear.core.config.interceptor.AcceptLanguageHeaderInterceptor;
import co.jinear.core.config.interceptor.LogExecutionInterceptor;
import co.jinear.core.system.gcloud.security.SecretManager;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor() throws IOException {
        String password = SecretManager.getLastSecretVersionValue("jasypt_encryptor_password");
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }


    @Bean(name = "htmlSanitizerPolicy")
    PolicyFactory htmlSanitizerPolicy() {
        final String TARGET = "target";
        final String _BLANK = "_blank";

        return new HtmlPolicyBuilder()
                .allowUrlProtocols("https")
                .allowElements((elementName, attrs) -> {
                    if ("a".equalsIgnoreCase(elementName)) {
                        int targetIndex = attrs.indexOf(TARGET);
                        if (targetIndex < 0) {
                            attrs.add(TARGET);
                            attrs.add(_BLANK);
                        } else {
                            attrs.set(targetIndex + 1, _BLANK);
                        }
                    }
                    return elementName;
                }, "h1", "h2", "h3", "h4", "h5", "h6", "p", "b", "i", "em", "strong", "a", "br", "li", "ul", "ol", "blockquote", "hr", "pre", "code")
                .allowAttributes("href", TARGET, "rel").onElements("a")
                .requireRelNofollowOnLinks()
                .toFactory();
    }

    @Bean(name = "htmlToPlainTextPolicy")
    PolicyFactory htmlToPlainTextPolicy() {
        return new HtmlPolicyBuilder()
                .toFactory();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogExecutionInterceptor());
        registry.addInterceptor(new AcceptLanguageHeaderInterceptor());
    }
}
