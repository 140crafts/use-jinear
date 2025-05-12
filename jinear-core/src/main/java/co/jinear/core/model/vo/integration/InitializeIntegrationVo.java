package co.jinear.core.model.vo.integration;

import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitializeIntegrationVo {

    private IntegrationProvider provider;
    private String accountId;
    private String relatedObjectId;
}
