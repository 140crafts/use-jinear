package co.jinear.core.model.request.account;

import co.jinear.core.model.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

@Getter
@Setter
public class UpdatePasswordRequest extends BaseRequest {
    @Nullable
    private String currentPassword;
    @NotNull
    @Size(min = 6, max = 512)
    private String newPassword;
}
