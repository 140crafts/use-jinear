package co.jinear.core.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterSort {
    IDATE_DESC(0),
    IDATE_ASC(1),
    ASSIGNED_DATE_DESC(2),
    ASSIGNED_DATE_ASC(3);

    private final int value;
}
