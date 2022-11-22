package co.jinear.core.model.vo.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteTeamMemberVo {
    private String accountId;
    private String teamId;
}
