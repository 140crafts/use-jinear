package co.jinear.core.service.notification;

import co.jinear.core.converter.notification.NotificationTemplateDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationTemplateType;
import co.jinear.core.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplateRetrieveService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationTemplateDtoConverter notificationTemplateDtoConverter;

    public NotificationTemplateDto retrieve(NotificationTemplateType templateType, LocaleType localeType) {
        log.info("Retrieve notification template has started. templateType: {}, localeType: {}", templateType, localeType);
        return notificationTemplateRepository.findByTemplateTypeAndLocaleTypeAndPassiveIdIsNull(templateType, localeType)
                .map(notificationTemplateDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }
}
