package co.jinear.core.model.enumtype.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionType {
    ACCOUNT_ROLE_EDIT("account-role:edit"),
    PROCESS_REMINDER_JOB("reminders:process"),
    EXPIRE_TEMP_PUBLIC_MEDIA("scheduled-jobs:expire-temporary-public-media");

    private final String permission;
}
