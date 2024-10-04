package co.jinear.core.model.response.project;

import co.jinear.core.model.dto.project.AccountProjectPermissionFlags;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountProjectPermissionFlagsResponse extends BaseResponse {

    @JsonProperty("data")
    private AccountProjectPermissionFlags accountProjectPermissionFlags;
}
