package co.jinear.core.model.enumtype.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectDomainType {
    AUTO_GENERATED(0),
    CUSTOM(1);

    private final int value;
}
