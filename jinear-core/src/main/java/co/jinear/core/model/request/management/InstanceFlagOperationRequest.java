package co.jinear.core.model.request.management;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstanceFlagOperationRequest extends BaseRequest {

    @NotNull
    private InstanceFlagType instanceFlagType;
    @NotNull
    private Object value;
}
