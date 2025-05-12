package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class SetCommunicationPermissionsRequest extends BaseRequest {

    @Nullable
    private Boolean email = Boolean.FALSE;
    @Nullable
    private Boolean pushNotification = Boolean.FALSE;
}
