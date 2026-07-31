package co.jinear.core.service.richtext.sync;

import co.jinear.core.model.entity.richtext.RichText;
import co.jinear.core.model.enumtype.richtext.RichTextType;

public interface RichTextSyncAuthStrategy {

    RichTextType supports();

    void validateCanRead(String accountId, RichText richText);

    void validateCanWrite(String accountId, RichText richText);
}
