package co.jinear.core.model.enumtype.team;

import lombok.Getter;

@Getter
public enum TeamActivityType {
    JOIN,
    LEAVE,
    KICKED_OUT,
    REQUESTED_ACCESS,
    PLACED_BET,
    WON_BET,
    LOST_BET;
}
