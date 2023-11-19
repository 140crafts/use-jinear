package co.jinear.core.model.dto.integration;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class IntegrationInfoDto extends BaseDto {

    private String integrationInfoId;
    private IntegrationProvider provider;
    private String accountId;
    private String relatedObjectId;
    private Set<IntegrationScopeDto> scopes;
}
