package co.jinear.core.repository;

import co.jinear.core.model.entity.google.GmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GmailMessageRepository extends JpaRepository<GmailMessage, String> {
}
