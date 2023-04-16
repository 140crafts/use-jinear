package co.jinear.core.repository;

import co.jinear.core.model.entity.notification.NotificationTemplate;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import co.jinear.core.model.enumtype.notification.NotificationTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String> {

    Optional<NotificationTemplate> findByTemplateTypeAndLocaleTypeAndPassiveIdIsNull(NotificationTemplateType templateType, LocaleType localeType);
}
