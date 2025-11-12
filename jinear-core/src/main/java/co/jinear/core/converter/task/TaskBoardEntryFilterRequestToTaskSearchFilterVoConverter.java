package co.jinear.core.converter.task;

import co.jinear.core.converter.team.TeamMembershipTeamVisibilityTypeMapConverter;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.enumtype.FilterSort;
import co.jinear.core.model.request.task.TaskBoardEntryFilterRequest;
import co.jinear.core.model.vo.task.TaskSearchFilterVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskBoardEntryFilterRequestToTaskSearchFilterVoConverter {

    @Autowired
    protected TeamMembershipTeamVisibilityTypeMapConverter teamMembershipTeamVisibilityTypeMapConverter;

    @Mapping(target = "teamMemberMap", expression = "java(teamMembershipTeamVisibilityTypeMapConverter.convert(memberships))")
    public abstract TaskSearchFilterVo convert(String taskBoardId, TaskBoardEntryFilterRequest taskBoardEntryFilterRequest, Integer page, List<TeamMemberDto> memberships);

    @AfterMapping
    void afterMap(@MappingTarget TaskSearchFilterVo taskSearchFilterVo, String taskBoardId) {
        taskSearchFilterVo.setTaskboardIds(List.of(taskBoardId));
        taskSearchFilterVo.setSort(FilterSort.TASK_BOARD_ORDER);
    }
}
