package co.jinear.core.converter.task;

import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.vo.task.TaskTagVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class TaskTagVoConverter {

    private static final String TAG_SEPARATOR = "-";

    public List<TaskTagVo> convert(List<String> taskTags, List<TeamMemberDto> memberships) {
        return Optional.ofNullable(taskTags)
                .filter(Predicate.not(List::isEmpty))
                .map(tags -> mapTaskTags(tags, memberships))
                .orElse(null);
    }

    private List<TaskTagVo> mapTaskTags(List<String> taskTags, List<TeamMemberDto> memberships) {
        Map<String, String> teamIdsByTag = mapTeamIdsByTag(memberships);
        return taskTags.stream()
                .map(this::normalize)
                .distinct()
                .map(taskTag -> mapTaskTag(taskTag, teamIdsByTag))
                .flatMap(Optional::stream)
                .toList();
    }

    private Map<String, String> mapTeamIdsByTag(List<TeamMemberDto> memberships) {
        return memberships.stream()
                .map(TeamMemberDto::getTeam)
                .collect(Collectors.toMap(TeamDto::getTag, TeamDto::getTeamId, this::keepFirst));
    }

    private Optional<TaskTagVo> mapTaskTag(String taskTag, Map<String, String> teamIdsByTag) {
        int separatorIndex = taskTag.lastIndexOf(TAG_SEPARATOR);
        String teamTag = taskTag.substring(0, separatorIndex);
        Integer teamTagNo = Integer.valueOf(taskTag.substring(separatorIndex + 1));
        return Optional.ofNullable(teamIdsByTag.get(teamTag))
                .map(teamId -> new TaskTagVo(teamId, teamTagNo));
    }

    private String normalize(String taskTag) {
        return taskTag.toLowerCase(Locale.ROOT);
    }

    private String keepFirst(String first, String second) {
        return first;
    }
}
