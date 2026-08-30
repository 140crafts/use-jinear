package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpAuthorizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface McpAuthorizationRequestRepository extends JpaRepository<McpAuthorizationRequest, String> {

    Optional<McpAuthorizationRequest> findByMcpAuthorizationRequestIdAndPassiveIdIsNull(String mcpAuthorizationRequestId);

    @Modifying
    @Query("delete from McpAuthorizationRequest r where r.expiresAt < :before")
    int deleteAllExpiredBefore(@Param("before") Date before);
}
