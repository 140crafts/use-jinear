package co.jinear.core.model.dto.integration;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.google.GoogleUserInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntegrationInfoDto extends BaseDto {

    private String integrationInfoId;
    private IntegrationProvider provider;
    private String accountId;
    private String relatedObjectId;
    private List<IntegrationScopeType> scopes;
    private GoogleUserInfoDto googleUserInfo;
}
