package co.jinear.core.service.calendar;

import co.jinear.core.converter.calendar.CalendarShareKeyDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.calendar.CalendarShareKeyDto;
import co.jinear.core.model.entity.calendar.CalendarShareKey;
import co.jinear.core.repository.calendar.CalendarShareKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarShareKeyService {

    private final CalendarShareKeyRepository calendarShareKeyRepository;
    private final CalendarShareKeyDtoConverter calendarShareKeyDtoConverter;

    public CalendarShareKeyDto retrieveShareableKey(String shareableKey) {
        log.info("Retrieve shareable key has started. shareableKey: {}", shareableKey);
        return calendarShareKeyRepository.findByShareableKeyAndPassiveIdIsNull(shareableKey)
                .map(calendarShareKeyDtoConverter::convert)
                .orElseThrow(NotFoundException::new);

    }

    public CalendarShareKeyDto retrieveShareableKey(String accountId, String workspaceId) {
        log.info("Retrieve shareable key has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        return calendarShareKeyRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId)
                .map(calendarShareKeyDtoConverter::convert)
                .orElseGet(() -> initializeShareableKey(accountId, workspaceId));
    }

    public void refreshShareableKey(String accountId, String workspaceId){
        log.info("Refresh shareable key has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        CalendarShareKey calendarShareKey = calendarShareKeyRepository.findByAccountIdAndWorkspaceIdAndPassiveIdIsNull(accountId, workspaceId).orElseThrow(NotFoundException::new);
        calendarShareKey.setShareableKey(UUID.randomUUID().toString());
        calendarShareKeyRepository.save(calendarShareKey);
    }

    private CalendarShareKeyDto initializeShareableKey(String accountId, String workspaceId) {
        log.info("Initialize shareable key has started. accountId: {}, workspaceId: {}", accountId, workspaceId);
        CalendarShareKey calendarShareKey = mapCalendarShareKey(accountId, workspaceId);
        CalendarShareKey saved = calendarShareKeyRepository.save(calendarShareKey);
        return calendarShareKeyDtoConverter.convert(saved);
    }

    private CalendarShareKey mapCalendarShareKey(String accountId, String workspaceId) {
        CalendarShareKey calendarShareKey = new CalendarShareKey();
        calendarShareKey.setAccountId(accountId);
        calendarShareKey.setWorkspaceId(workspaceId);
        calendarShareKey.setShareableKey(UUID.randomUUID().toString());
        return calendarShareKey;
    }
}
