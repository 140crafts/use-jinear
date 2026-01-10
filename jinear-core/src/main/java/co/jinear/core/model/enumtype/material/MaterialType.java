package co.jinear.core.model.enumtype.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialType {

    FILE(0),
    FOLDER(1);

    private final int value;
}
