package co.jinear.core.converter.task;

import co.jinear.core.model.request.task.RetrieveIntersectingTasksFromTeamRequest;
import co.jinear.core.model.request.task.RetrieveIntersectingTasksFromWorkspaceRequest;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromTeamVo;
import co.jinear.core.model.vo.task.SearchIntersectingTasksFromWorkspaceVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchIntersectingTasksVoConverter {

    SearchIntersectingTasksFromTeamVo map(RetrieveIntersectingTasksFromTeamRequest retrieveIntersectingTasksFromTeamRequest);

    SearchIntersectingTasksFromWorkspaceVo map(RetrieveIntersectingTasksFromWorkspaceRequest retrieveIntersectingTasksFromWorkspaceRequest, List<String> teamIdList);
}
