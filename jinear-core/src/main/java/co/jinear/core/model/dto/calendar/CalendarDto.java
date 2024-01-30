package co.jinear.core.model.dto.calendar;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.model.enumtype.integration.IntegrationScopeType;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class CalendarDto {

    private String calendarId;
    private String workspaceId;
    private String initializedBy;
    private String integrationInfoId;
    private IntegrationInfoDto integrationInfo;
    private String name;
    private IntegrationProvider provider;
    private List<IntegrationScopeType> scopes;
    @Nullable
    private List<ExternalCalendarSourceDto> calendarSources;
}
