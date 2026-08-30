package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpOauthClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface McpOauthClientRepository extends JpaRepository<McpOauthClient, String> {

    Optional<McpOauthClient> findByClientIdAndPassiveIdIsNull(String clientId);

    Page<McpOauthClient> findAllByPassiveIdIsNullOrderByCreatedDateDesc(Pageable pageable);
}
