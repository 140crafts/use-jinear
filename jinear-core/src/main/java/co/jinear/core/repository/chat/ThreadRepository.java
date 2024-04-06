package co.jinear.core.repository.chat;

import co.jinear.core.model.entity.chat.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, String> {
}
