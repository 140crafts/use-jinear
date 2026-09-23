package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpToolCallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface McpToolCallLogRepository extends JpaRepository<McpToolCallLog, String> {

    Page<McpToolCallLog> findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, Pageable pageable);

    Page<McpToolCallLog> findAllByOauthConnectionIdAndPassiveIdIsNullOrderByCreatedDateDesc(String oauthConnectionId, Pageable pageable);

    Page<McpToolCallLog> findAllByPassiveIdIsNullOrderByCreatedDateDesc(Pageable pageable);

    @Query("""
            select l.toolName as toolName,
                   count(l) as callCount,
                   sum(case when l.callStatus = co.jinear.core.model.enumtype.mcp.McpToolCallStatus.OK then 0L else 1L end) as errorCount,
                   coalesce(sum(l.durationMs), 0) as totalDurationMs
            from McpToolCallLog l
            where l.createdDate >= :from and (:workspaceId is null or l.workspaceId = :workspaceId)
            group by l.toolName
            order by count(l) desc
            """)
    List<McpToolUsageProjection> summarizeByTool(@Param("from") Date from, @Param("workspaceId") String workspaceId);

    @Modifying
    @Query("delete from McpToolCallLog l where l.createdDate < :before")
    int deleteAllOlderThan(@Param("before") Date before);

    interface McpToolUsageProjection {
        String getToolName();

        Long getCallCount();

        Long getErrorCount();

        Long getTotalDurationMs();
    }
}
