package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface McpRefreshTokenRepository extends JpaRepository<McpRefreshToken, String> {

    Optional<McpRefreshToken> findByMcpRefreshTokenIdAndPassiveIdIsNull(String mcpRefreshTokenId);

    List<McpRefreshToken> findAllByMcpConnectionIdAndPassiveIdIsNull(String mcpConnectionId);
}
