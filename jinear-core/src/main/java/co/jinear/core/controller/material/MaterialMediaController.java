package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialMediaManager;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material/media")
public class MaterialMediaController {

    private final MaterialMediaManager materialMediaManager;

    @GetMapping(value = "/{materialId}")
    @ResponseStatus(HttpStatus.OK)
    public void downloadMaterialMedia(@PathVariable String materialId,
                                      HttpServletResponse response) throws IOException {
        materialMediaManager.downloadMaterialMedia(response, materialId);
    }

    @PostMapping(value = "/{materialId}/update-visibility/{mediaVisibilityType}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse changeRelatedMediaAccess(@PathVariable String materialId,
                                                 @PathVariable MediaVisibilityType mediaVisibilityType) throws IOException {
        return materialMediaManager.changeRelatedMediaAccess(materialId, mediaVisibilityType);
    }
}
