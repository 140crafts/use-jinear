package co.jinear.core.model.enumtype.richtext;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RichTextFormat {

    LEGACY(0),
    CRDT(1);

    private final int value;

    RichTextFormat(int value) {
        this.value = value;
    }

    public static RichTextFormat fromValue(int value) {
        return Arrays.stream(values())
                .filter(format -> format.value == value)
                .findFirst()
                .orElse(LEGACY);
    }
}
