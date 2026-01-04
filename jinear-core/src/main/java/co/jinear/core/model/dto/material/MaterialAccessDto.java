package co.jinear.core.model.dto.material;

import co.jinear.core.model.dto.BaseDto;
import co.jinear.core.model.dto.account.PlainAccountProfileDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialAccessDto extends BaseDto {
    private String materialAccessId;
    private String materialId;
    private String accountId;
    private PlainAccountProfileDto account;
}
