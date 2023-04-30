package co.jinear.core.model.dto.task;

import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSubscriptionWithCommunicationPreferencesDto {

    private String accountId;
    private String email;
    private LocaleType localeType;
    private String timeZone;
    private Boolean hasEmailPermission;
    private Boolean hasPushNotificationPermission;
}
