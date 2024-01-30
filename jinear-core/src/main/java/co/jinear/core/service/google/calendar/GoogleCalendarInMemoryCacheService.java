package co.jinear.core.service.google.calendar;

import co.jinear.core.model.dto.calendar.ExternalCalendarSourceDto;
import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.service.cache.InMemoryCacheService;
import co.jinear.core.system.gcloud.googleapis.model.calendar.request.RetrieveEventListRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarInMemoryCacheService {

    private static final Long TTL = 2L;
    private static final String APP = "jinear:";
    private static final String PREFIX = "gcal:";
    private static final String GOOGLE_CALENDAR_INFO_LIST = "calendar-infos:";
    private static final String GOOGLE_CALENDAR_EVENT_LIST = "calendar-events:";

    private final InMemoryCacheService inMemoryCacheService;

    public boolean hasCachedCalendarSourceList(String googleUserInfoId) {
        String key = generateCalendarSourceListKey(googleUserInfoId);
        return Boolean.TRUE.equals(inMemoryCacheService.hasKey(key));
    }

    public List<ExternalCalendarSourceDto> cacheCalendarSourceList(String googleUserInfoId, List<ExternalCalendarSourceDto> externalCalendarSourceDtos) {
        String key = generateCalendarSourceListKey(googleUserInfoId);
        inMemoryCacheService.put(key, externalCalendarSourceDtos, Duration.ofMinutes(TTL).toSeconds());
        return externalCalendarSourceDtos;
    }

    public List<ExternalCalendarSourceDto> getCachedCalendarSourceList(String googleUserInfoId) {
        log.info("Get cached calendar source list has started.");
        String key = generateCalendarSourceListKey(googleUserInfoId);
        return cast(inMemoryCacheService.get(key));
    }

    private String generateCalendarSourceListKey(String googleUserInfoId) {
        return APP + PREFIX + GOOGLE_CALENDAR_INFO_LIST + googleUserInfoId;
    }

    public boolean hasCachedCalendarEventList(RetrieveEventListRequest retrieveEventListRequest) {
        String key = generateCalendarEventsKey(retrieveEventListRequest);
        return Boolean.TRUE.equals(inMemoryCacheService.hasKey(key));
    }

    public List<TaskDto> cacheCalendarEventList(RetrieveEventListRequest retrieveEventListRequest, List<TaskDto> taskDtoList) {
        String key = generateCalendarEventsKey(retrieveEventListRequest);
        inMemoryCacheService.put(key, taskDtoList, Duration.ofMinutes(TTL).toSeconds());
        return taskDtoList;
    }

    public List<TaskDto> getCachedCalendarEventList(RetrieveEventListRequest retrieveEventListRequest) {
        log.info("Get cached calendar event list has started.");
        String key = generateCalendarEventsKey(retrieveEventListRequest);
        return cast(inMemoryCacheService.get(key));
    }

    private String generateCalendarEventsKey(RetrieveEventListRequest retrieveEventListRequest) {
        return APP + PREFIX + GOOGLE_CALENDAR_EVENT_LIST + retrieveEventListRequest.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }
}
