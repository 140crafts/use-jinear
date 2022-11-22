package co.jinear.core.model.enumtype.passive;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassiveReason {
    SYSTEM(-1),
    USER_ACTION(0),
    FREEZE_ACCOUNT(1),
    DELETE_ACCOUNT(2),
    BANNED_ACCOUNT(30),
    SUSPENDED_ACCOUNT(31),

    REQUEST_RESPONSE(4),
    SMS_LOGIN_TOKEN_USED(5),
    PHONE_CHANGED(6),
    EMAIL_LOGIN_TOKEN_EXPIRED(7),
    EMAIL_LOGIN_TOKEN_USED(8),
    EMAIL_ATTACH_TOKEN_USED(9),
    REMOVE_FEATURE(10),
    REPORT_RESOLVE_GUILTY(11),
    REPORT_RESOLVE_NOT_GUILTY(12),
    TICKET_RESOLVE(200),
    WAIT_LIST_PASSCODE_USED(300),

    PROFILE_PIC_UPDATE(400),

    UNFOLLOW(13),

    PAYMENT_ISSUE(500);

    private int id;
}