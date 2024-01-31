package co.jinear.core.converter.calendar;

import co.jinear.core.model.dto.calendar.TaskExternalCalendarFilterDto;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import co.jinear.core.system.util.ZonedDateHelper;
import org.springframework.stereotype.Component;

@Component
public class RetrieveEventListRequestConverter {

    public RetrieveEventListRequest convert(TaskSearchFilterVo taskSearchFilterVo, TaskExternalCalendarFilterDto taskExternalCalendarFilterDto) {
        RetrieveEventListRequest retrieveEventListRequest = new RetrieveEventListRequest();
        retrieveEventListRequest.setCalendarSourceId(taskExternalCalendarFilterDto.getCalendarSourceId());
        retrieveEventListRequest.setTimeMin(ZonedDateHelper.formatWithDateTimeFormat5(taskSearchFilterVo.getTimespanStart()));
        retrieveEventListRequest.setTimeMax(ZonedDateHelper.formatWithDateTimeFormat5(taskSearchFilterVo.getTimespanEnd()));
        return retrieveEventListRequest;
    }
}
