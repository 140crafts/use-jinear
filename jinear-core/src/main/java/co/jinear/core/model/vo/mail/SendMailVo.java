package co.jinear.core.model.vo.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMailVo {
    private String to;
    private String subject;
    private String context;
    private String attachment;
    private String attachmentFileName;
    private String attachmentContentType;

    public SendMailVo(String to, String subject, String context) {
        this.to = to;
        this.subject = subject;
        this.context = context;
    }
}
