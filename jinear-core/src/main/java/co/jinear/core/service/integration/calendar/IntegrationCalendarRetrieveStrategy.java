package co.jinear.core.service.integration.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.integration.IntegrationInfoDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.enumtype.integration.IntegrationProvider;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;

import java.util.List;

public interface IntegrationCalendarRetrieveStrategy {

    IntegrationProvider getProvider();

    List<ExternalCalendarSourceDto> retrieveCalendarSources(IntegrationInfoDto integrationInfoDto);

    List<TaskDto> retrieveCalendarEvents(IntegrationInfoDto integrationInfoDto, RetrieveEventListRequest retrieveEventListRequest);
}
