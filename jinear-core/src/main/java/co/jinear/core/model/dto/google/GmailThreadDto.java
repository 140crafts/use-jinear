package co.jinear.core.model.dto.google;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GmailThreadDto extends BaseDto {

    private String gmailThreadId;
    private String googleTokenId;
    private String gId;
    private String gHistoryId;
    private String snippet;
//    private Set<GmailMessageDto> messages;
}
