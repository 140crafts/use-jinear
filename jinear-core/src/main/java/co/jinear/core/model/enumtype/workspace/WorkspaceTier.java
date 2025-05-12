package co.jinear.core.model.enumtype.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkspaceTier {

    BASIC(0),
    PRO(1);

    private final int value;
}
