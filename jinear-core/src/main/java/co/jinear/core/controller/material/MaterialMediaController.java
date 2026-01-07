package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialMediaManager;
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
}
