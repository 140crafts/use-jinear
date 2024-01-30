package co.jinear.core.model.dto.calendar;

import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntegrationExternalCalendarSourceListDto {

    private IntegrationInfoDto integrationInfo;
    private List<ExternalCalendarSourceDto> calendarSources;
}
