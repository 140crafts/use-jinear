package co.jinear.core.converter.task;

import co.jinear.core.model.dto.task.TaskDto;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.system.NormalizeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TaskTagConverter {

    public String retrieveTaskTag(TaskDto taskDto) {
        StringBuilder taskTagSb = new StringBuilder();
        Optional.of(taskDto)
                .map(TaskDto::getTeam)
                .map(TeamDto::getTag)
                .ifPresent(teamTag -> taskTagSb.append(teamTag).append(NormalizeHelper.HYPHEN));
        Optional.of(taskDto)
                .map(TaskDto::getTeamTagNo)
                .map(Object::toString)
                .ifPresent(taskTagSb::append);
        return taskTagSb.toString();
    }
}
