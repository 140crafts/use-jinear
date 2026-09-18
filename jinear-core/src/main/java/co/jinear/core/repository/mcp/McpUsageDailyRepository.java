package co.jinear.core.repository.mcp;

import co.jinear.core.model.entity.mcp.McpUsageDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface McpUsageDailyRepository extends JpaRepository<McpUsageDaily, String> {

    List<McpUsageDaily> findAllByUsageDateGreaterThanEqualAndPassiveIdIsNull(LocalDate from);

    List<McpUsageDaily> findAllByUsageDateGreaterThanEqualAndWorkspaceIdAndPassiveIdIsNull(LocalDate from, String workspaceId);

    boolean existsByUsageDate(LocalDate usageDate);
}
