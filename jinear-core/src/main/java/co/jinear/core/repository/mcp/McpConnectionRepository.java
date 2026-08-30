package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface McpConnectionRepository extends JpaRepository<McpConnection, String> {

    Optional<McpConnection> findByMcpConnectionIdAndPassiveIdIsNull(String mcpConnectionId);

    List<McpConnection> findAllByAccountIdAndPassiveIdIsNullOrderByCreatedDateDesc(String accountId);

    Optional<McpConnection> findFirstByAccountIdAndClientIdAndPassiveIdIsNull(String accountId, String clientId);

    long countByPassiveIdIsNull();
}
