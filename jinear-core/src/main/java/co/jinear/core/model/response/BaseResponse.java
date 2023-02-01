package co.jinear.core.model.response;

import co.jinear.core.model.enumtype.ResponseStatusType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseResponse {
    private ResponseStatusType responseStatusType = ResponseStatusType.SUCCESS;
    private String errorCode;
    private String errorMessage;
    private String errorGroup;
    private String consumerErrorMessage;
    private String responseLocale;
    private long systemTime = System.currentTimeMillis();
    private String conversationId;
}
