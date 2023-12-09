package co.jinear.core.model.dto.feed;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedDto extends BaseDto {

    private String feedId;
    private String workspaceId;
    private String initializedBy;
    private String integrationInfoId;
    private String name;
    private IntegrationProvider provider;
    private List<IntegrationScopeType> scopes;
}
