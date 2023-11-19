package co.jinear.core.model.enumtype.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IntegrationScopeType {

    LOGIN(0),
    EMAIL(0),
    CALENDAR(0);

    private final int value;
}
