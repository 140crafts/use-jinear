package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialManager;
import co.jinear.core.model.response.material.MaterialRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material")
public class MaterialController {

    private final MaterialManager materialManager;

    @GetMapping("/{materialId}")
    @ResponseStatus(HttpStatus.OK)
    public MaterialRetrieveResponse retrieve(@PathVariable String materialId) {
        return materialManager.retrieve(materialId);
    }
}
