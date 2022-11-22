package co.jinear.core.model.enumtype.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionType {
    ACCOUNT_ROLE_EDIT("account-role:edit");

    private final String permission;
}
