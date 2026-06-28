package co.jinear.core.model.enumtype.notebook;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum NotebookVisibilityType {

    PRIVATE(0),
    SHARED(1);

    private final int value;

    NotebookVisibilityType(int value) {
        this.value = value;
    }

    public static NotebookVisibilityType fromValue(int value) {
        return Arrays.stream(values())
                .filter(visibility -> visibility.value == value)
                .findFirst()
                .orElse(PRIVATE);
    }
}
