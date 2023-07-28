package co.jinear.core.service.notification.provider;

import co.jinear.core.config.OneSignalConfig;
import co.jinear.core.model.enumtype.notification.NotificationProviderType;
import co.jinear.core.model.vo.notification.NotificationMessageVo;
import com.onesignal.client.api.DefaultApi;
import com.onesignal.client.model.CreateNotificationSuccessResponse;
import com.onesignal.client.model.Notification;
import com.onesignal.client.model.StringMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneSignalNotificationDeliveryStrategy implements NotificationDeliveryStrategy {

    private final OneSignalConfig oneSignalConfig;
    private final DefaultApi oneSignalApiClient;

    @Override
    public void send(NotificationMessageVo notificationMessageVo) throws Exception {
        log.info("One signal notification send has started. notificationMessageVo: {}", notificationMessageVo);
        Notification notification = createNotification(notificationMessageVo);
        CreateNotificationSuccessResponse response = oneSignalApiClient.createNotification(notification);
        log.info("One signal notification created. oneSignalNotificationId: {}", response.getId());
    }

    @Override
    public NotificationProviderType getProviderType() {
        return NotificationProviderType.ONE_SIGNAL;
    }

    private Notification createNotification(NotificationMessageVo notificationMessageVo) {
        Notification notification = new Notification();
        notification.setAppId(oneSignalConfig.getAppId());
        notification.setIncludePlayerIds(List.of(notificationMessageVo.getTarget().getExternalTargetId()));
        notification.setUrl(notificationMessageVo.getLaunchUrl());
        notification.setData(notificationMessageVo.getData());

        StringMap titleMap = new StringMap();
        titleMap.en(notificationMessageVo.getTitle());
        notification.setContents(titleMap);

        StringMap subtitleMap = new StringMap();
        subtitleMap.en(notificationMessageVo.getText());
        notification.setSubtitle(subtitleMap);

        return notification;
    }
}
