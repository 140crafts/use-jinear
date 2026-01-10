package co.jinear.core.model.response.material;

import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialRetrieveResponse extends BaseResponse {

    @JsonProperty("data")
    private MaterialDto materialDto;
}
