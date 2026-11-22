package co.jinear.core.task;

import co.jinear.core.converter.task.TaskTagVoConverter;
import co.jinear.core.model.dto.team.TeamDto;
import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.vo.task.TaskTagVo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class TaskTagVoConverterTest {

    private final TaskTagVoConverter taskTagVoConverter = new TaskTagVoConverter();
    private final List<TeamMemberDto> memberships = List.of(membership("team-1", "jin"), membership("team-2", "ds-ticaret"));

    @Test
    void aTagOfAMemberTeamResolvesToItsTeamIdAndNumber() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("jin-585"), memberships);

        assertThat(taskTags).extracting(TaskTagVo::getTeamId, TaskTagVo::getTeamTagNo).containsExactly(tuple("team-1", 585));
    }

    @Test
    void aTeamTagWithAHyphenSplitsAtTheLastHyphen() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("ds-ticaret-12"), memberships);

        assertThat(taskTags).extracting(TaskTagVo::getTeamId, TaskTagVo::getTeamTagNo).containsExactly(tuple("team-2", 12));
    }

    @Test
    void anUppercaseTagMatchesTheLowercaseTeamTag() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("JIN-585"), memberships);

        assertThat(taskTags).extracting(TaskTagVo::getTeamId).containsExactly("team-1");
    }

    @Test
    void duplicateTagsResolveOnce() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("jin-585", "JIN-585"), memberships);

        assertThat(taskTags).hasSize(1);
    }

    @Test
    void aTagOfATeamTheAccountIsNotAMemberOfIsDropped() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("hidden-3", "jin-585"), memberships);

        assertThat(taskTags).extracting(TaskTagVo::getTeamId).containsExactly("team-1");
    }

    @Test
    void onlyUnknownTagsGiveAnEmptyListThatMatchesNoTask() {
        List<TaskTagVo> taskTags = taskTagVoConverter.convert(List.of("hidden-3"), memberships);

        assertThat(taskTags).isEmpty();
    }

    @Test
    void noTagsGiveNoFilter() {
        assertThat(taskTagVoConverter.convert(null, memberships)).isNull();
        assertThat(taskTagVoConverter.convert(List.of(), memberships)).isNull();
    }

    private TeamMemberDto membership(String teamId, String tag) {
        TeamDto teamDto = new TeamDto();
        teamDto.setTeamId(teamId);
        teamDto.setTag(tag);
        TeamMemberDto teamMemberDto = new TeamMemberDto();
        teamMemberDto.setTeamId(teamId);
        teamMemberDto.setTeam(teamDto);
        return teamMemberDto;
    }
}
