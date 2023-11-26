package co.jinear.core.model.dto.integration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class IntegrationFeedDto<T> {

    private Page<T> data;
}
