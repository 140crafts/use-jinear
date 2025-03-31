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
public class MinIoProperties {

    @Value("${minio.base-path}")
    private String basePath;

    @Value("${minio.private-bucket-name}")
    private String privateBucketName;

    @Value("${minio.public-bucket-name}")
    private String publicBucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.key}")
    private String key;

    @Value("${minio.secret}")
    private String secret;
}
