package co.jinear.core.model.enumtype.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelSettingType {

    SYNC_MEMBERS_WITH_TEAM(0);

    private final int value;
}
