package co.jinear.core.system.gcloud.googleapis.model;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

@Getter
@Setter
public class GmailThreadVo {

    @Nullable
    private String id;
    @Nullable
    private String historyId;
    @Nullable
    private String snippet;
    @Nullable
    private List<GmailMessageVo> messages;
}
