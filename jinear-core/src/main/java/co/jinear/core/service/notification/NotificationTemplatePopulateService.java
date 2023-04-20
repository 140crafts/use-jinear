package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationEventDto;
import co.jinear.core.model.dto.notification.NotificationEventParamDto;
import co.jinear.core.model.dto.notification.NotificationMessageDto;
import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplatePopulateService {

    public NotificationMessageDto populate(NotificationEventDto notificationEventDto) {
        log.info("Populate notification message has started. notificationEventDto: {}", notificationEventDto);

        NotificationTemplateDto template = notificationEventDto.getTemplate();

        Map<String, String> paramMap = new HashMap<>();
        Set<NotificationEventParamDto> params = notificationEventDto.getParams();
        params.forEach(notificationEventParam -> paramMap.put(notificationEventParam.getParamKey(), notificationEventParam.getParamValue()));

        StringSubstitutor sub = new StringSubstitutor(paramMap);
        String title = sub.replace(template.getTitle());
        String text = sub.replace(template.getText());
        String launchUrl = sub.replace(template.getLaunchUrl());

        NotificationMessageDto notificationMessageDto = new NotificationMessageDto();
        notificationMessageDto.setTitle(title);
        notificationMessageDto.setText(text);
        notificationMessageDto.setLaunchUrl(launchUrl);

        notificationMessageDto.setNotificationEventId(notificationEventDto.getNotificationEventId());
        notificationMessageDto.setCreatedDate(notificationEventDto.getCreatedDate());
        notificationMessageDto.setLastUpdatedDate(notificationEventDto.getLastUpdatedDate());
        notificationMessageDto.setPassiveId(notificationEventDto.getPassiveId());

        Optional.of(notificationEventDto)
                .map(NotificationEventDto::getTemplate)
                .map(NotificationTemplateDto::getTemplateType)
                .ifPresent(notificationMessageDto::setTemplateType);

        log.info("Populate notification message has completed. notificationEventDto: {}", notificationEventDto);
        return notificationMessageDto;
    }

    public NotificationMessageVo populate(NotificationTemplateDto notificationTemplateDto, NotificationSendVo notificationSendVo) {
        log.info("Populate notification template with given params has started. notificationSendVo: {}", notificationSendVo);
        final LocaleType localeType = notificationSendVo.getLocaleType();

        StringSubstitutor sub = new StringSubstitutor(notificationSendVo.getParams());
        String title = sub.replace(notificationTemplateDto.getTitle());
        String text = sub.replace(notificationTemplateDto.getText());
        String launchUrl = sub.replace(notificationTemplateDto.getLaunchUrl());

        NotificationMessageVo notificationMessageVo = new NotificationMessageVo();
        notificationMessageVo.setTitle(title);
        notificationMessageVo.setText(text);
        notificationMessageVo.setLaunchUrl(launchUrl);
        notificationMessageVo.setLocaleType(localeType);

        log.info("Notification template populate has completed. notificationMessageVo: {}", notificationMessageVo);
        return notificationMessageVo;
    }
}
