package co.jinear.core.controller.account;

import co.jinear.core.manager.account.AccountMediaManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/account/media")
public class AccountMediaController {

    private final AccountMediaManager accountMediaManager;

    @PostMapping(value = "/profile-picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateProfilePicture(@RequestParam("file") MultipartFile file) {
        return accountMediaManager.changeAccountProfilePicture(file);
    }
}