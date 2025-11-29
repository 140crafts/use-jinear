package co.jinear.core.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Value("${spring.data.redis.timeout}")
    private Long redisTimeout;

    @Value("${spring.data.redis.ssl}")
    private Boolean redisUseSsl;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(0);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            redisConfig.setPassword(redisPassword);
        }
        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfig =
                LettuceClientConfiguration.builder()
                        .commandTimeout(Duration.ofMillis(redisTimeout));

        if (Boolean.TRUE.equals(redisUseSsl)) {
            clientConfig.useSsl();
        }
        return new LettuceConnectionFactory(redisConfig, clientConfig.build());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> rateLimitRedisConnection() {
        RedisURI.Builder uriBuilder = RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .withTimeout(Duration.ofMillis(redisTimeout));

        if (redisPassword != null && !redisPassword.isEmpty()) {
            uriBuilder.withPassword(redisPassword.toCharArray());
        }

        if (Boolean.TRUE.equals(redisUseSsl)) {
            uriBuilder.withSsl(true);
        }

        RedisClient redisClient = RedisClient.create(uriBuilder.build());
        RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);
        return redisClient.connect(codec);
    }
}
