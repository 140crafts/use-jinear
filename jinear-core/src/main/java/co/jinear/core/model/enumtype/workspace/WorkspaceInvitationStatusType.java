package co.jinear.core.model.enumtype.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceInvitationStatusType {

    WAITING_FOR_ANSWER(0),
    ACCEPTED(1),
    DECLINED(2),
    TIMED_OUT(3);

    private final int value;
}
