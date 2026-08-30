package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpAuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface McpAuthorizationCodeRepository extends JpaRepository<McpAuthorizationCode, String> {

    Optional<McpAuthorizationCode> findByMcpAuthorizationCodeIdAndPassiveIdIsNull(String mcpAuthorizationCodeId);

    @Modifying
    @Query("delete from McpAuthorizationCode c where c.expiresAt < :before")
    int deleteAllExpiredBefore(@Param("before") Date before);
}
