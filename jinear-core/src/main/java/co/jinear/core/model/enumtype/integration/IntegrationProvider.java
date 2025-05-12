package co.jinear.core.model.enumtype.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IntegrationProvider {

    GOOGLE(0);

    private final int value;
}
