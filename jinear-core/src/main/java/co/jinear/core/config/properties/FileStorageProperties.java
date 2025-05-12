package co.jinear.core.config.properties;

import co.jinear.core.model.enumtype.media.MediaFileProviderType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class FileStorageProperties {

    @Value("${file-storage.active}")
    private MediaFileProviderType activeFileStorageType;

    @Value("${file-storage.explicit-content-check-enabled}")
    private Boolean explicitContentCheckEnabled;
}
