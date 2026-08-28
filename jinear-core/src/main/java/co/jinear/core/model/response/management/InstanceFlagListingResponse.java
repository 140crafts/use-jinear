package co.jinear.core.model.response.management;

import co.jinear.core.model.enumtype.management.InstanceFlagType;
import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class InstanceFlagListingResponse extends BaseResponse {

    @JsonProperty("data")
    private Map<InstanceFlagType, Object> instanceFlagTypeObjectMap;
}
