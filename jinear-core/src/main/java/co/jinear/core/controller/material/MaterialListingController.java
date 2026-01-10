package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.model.request.material.MaterialSearchRequest;
import co.jinear.core.model.response.material.ParentMaterialDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material/list")
public class MaterialListingController {

    private final MaterialListingManager materialListingManager;

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ParentMaterialDtoResponse search(@Valid @RequestBody MaterialSearchRequest request) {
        return materialListingManager.search(request);
    }
}
