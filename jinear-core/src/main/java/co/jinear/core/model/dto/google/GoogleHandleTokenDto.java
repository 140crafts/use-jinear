package co.jinear.core.model.dto.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleHandleTokenDto {

    private GoogleUserInfoDto googleUserInfoDto;
    private String passiveIdForScopeDeletion;
}
