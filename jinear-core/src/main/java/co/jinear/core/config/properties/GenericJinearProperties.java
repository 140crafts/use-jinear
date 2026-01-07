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
public class GenericJinearProperties {

    @Value("${jinear.remove-pricing}")
    private Boolean removePricing = Boolean.FALSE;

    @Value("${jinear.storage.limit.basic}")
    private Long storageLimitBasic;

    @Value("${jinear.storage.limit.pro}")
    private Long storageLimitPro;
}
