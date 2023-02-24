package co.jinear.core.model.enumtype.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionType {
    ACCOUNT_ROLE_EDIT("account-role:edit"),
    PROCESS_REMINDER_JOB("reminders:process");

    private final String permission;
}
