package co.jinear.core.model.dto.account;

import co.jinear.core.model.dto.team.member.TeamMemberDto;
import co.jinear.core.model.dto.team.workflow.TeamWorkflowStatusDto;
import co.jinear.core.model.dto.topic.TopicDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AccountContextTeamDto {

    private TeamMemberDto membership;
    private List<TeamWorkflowStatusDto> workflowStatuses;
    private List<TopicDto> topics;
}
