package co.jinear.core.service.notification;

import co.jinear.core.model.dto.notification.NotificationTemplateDto;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import co.jinear.core.model.vo.notification.NotificationSendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationTemplatePopulateService {

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
