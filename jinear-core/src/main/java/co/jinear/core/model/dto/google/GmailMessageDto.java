package co.jinear.core.model.dto.google;

import co.jinear.core.model.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GmailMessageDto extends BaseDto {

    private String gmailMessageId;
    private String gmailThreadId;
    private String gId;
    private String gThreadId;
    private String gHistoryId;
    private String gInternalDate;
    private String snippet;
    private String from;
    private String to;
    private String subject;
    private String body;
}
