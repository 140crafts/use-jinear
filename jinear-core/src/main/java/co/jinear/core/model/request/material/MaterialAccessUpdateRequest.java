package co.jinear.core.model.request.material;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MaterialAccessUpdateRequest extends BaseRequest {

    @NotNull
    private List<String> accountIds;
}
