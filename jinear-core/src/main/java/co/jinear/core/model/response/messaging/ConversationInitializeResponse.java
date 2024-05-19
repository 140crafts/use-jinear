package co.jinear.core.model.response.messaging;

import co.jinear.core.model.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationInitializeResponse extends BaseResponse {

    @JsonProperty("data")
    private String conversationId;
}
