package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectDomainCnameCheckResultType {
    CNAME_CHECK_OK_READY_FOR_MANAGER(0),
    CNAME_CHECK_FAILED(1),
    SETUP_COMPLETED(2),
    CANCELLED_CNAME_NOT_SETUP_IN_TIME(3);

    private final int value;
}
