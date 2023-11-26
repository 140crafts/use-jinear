package co.jinear.core.model.enumtype.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamTaskSourceType {
    APP(0),
    INTEGRATION(1);

    private final int value;
}
