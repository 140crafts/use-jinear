package co.jinear.core.model.dto.integration;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationScopeDto extends BaseDto {

    private String integrationScopeId;
    private String integrationInfoId;
    private IntegrationScopeType scope;
    private IntegrationInfoDto integrationInfo;
}
