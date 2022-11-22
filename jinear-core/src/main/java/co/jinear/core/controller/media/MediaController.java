package co.jinear.core.controller.media;

import co.jinear.core.manager.media.MediaManager;
import co.jinear.core.model.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/media")
public class MediaController {

    private final MediaManager mediaManager;

    @PostMapping(value = "/account/profile-picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateProfilePicture(@RequestParam("file") MultipartFile file) {
        return mediaManager.changeAccountProfilePicture(file);
    }

    @PostMapping(value = "/team/{teamId}/profile-picture", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse updateTeamProfilePicture(@PathVariable("teamId") String teamId,
                                                 @RequestParam("file") MultipartFile file) {
        return mediaManager.changeTeamProfilePicture(file, teamId);
    }
}