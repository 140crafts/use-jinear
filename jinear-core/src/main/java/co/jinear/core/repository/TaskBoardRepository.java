package co.jinear.core.repository;

import co.jinear.core.model.entity.task.TaskBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskBoardRepository extends JpaRepository<TaskBoard, String> {

    Optional<TaskBoard> findByTaskBoardIdAndPassiveIdIsNull(String taskBoardId);

    List<TaskBoard> findAllByTaskBoardIdInAndPassiveIdIsNull(List<String> taskBoardIdList);

    Page<TaskBoard> findAllByWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, Pageable pageable);

    Page<TaskBoard> findAllByTaskBoardIdNotInAndWorkspaceIdAndTeamIdAndPassiveIdIsNullOrderByCreatedDateDesc(List<String> excludingBoardIds, String workspaceId, String teamId, Pageable pageable);

    Page<TaskBoard> findAllByWorkspaceIdAndTitleLikeIgnoreCaseAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String filterRecentsByName, Pageable pageable);

    Page<TaskBoard> findAllByWorkspaceIdAndTeamIdAndTitleLikeIgnoreCaseAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, String teamId, String filterRecentsByName, Pageable pageable);

    Page<TaskBoard> findAllByTaskBoardIdNotInAndWorkspaceIdAndTeamIdAndTitleLikeIgnoreCaseAndPassiveIdIsNullOrderByCreatedDateDesc(List<String> excludingBoardIds, String workspaceId, String teamId, String filterRecentsByName, Pageable pageable);

    long countAllByWorkspaceIdAndTeamIdInAndTaskBoardIdInAndPassiveIdIsNull(String workspaceId, List<String> teamIds, List<String> taskBoardId);
}
