package co.jinear.core.model.vo.passive;

import co.jinear.core.model.enumtype.passive.PassiveReason;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreatePassiveVo {
    private String responsibleAccountId;
    private String relatedObjectId;
    private String reason;
    private PassiveReason reasonType;
    private PassiveReason reasonSubtype;
}
