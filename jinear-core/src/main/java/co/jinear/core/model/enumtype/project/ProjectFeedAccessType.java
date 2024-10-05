package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectFeedAccessType {
    PRIVATE(0),
    PUBLIC(1),
    GUESTS_ONLY(2);

    private final int value;
}
