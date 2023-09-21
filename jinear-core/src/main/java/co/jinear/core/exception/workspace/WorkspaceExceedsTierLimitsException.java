package co.jinear.core.exception.workspace;

import co.jinear.core.exception.BusinessException;

public class WorkspaceExceedsTierLimitsException extends BusinessException {

    public WorkspaceExceedsTierLimitsException() {
        super("workspace.tier.exceeds-limits");
    }

    public WorkspaceExceedsTierLimitsException(String message) {
        super(message);
    }
}
