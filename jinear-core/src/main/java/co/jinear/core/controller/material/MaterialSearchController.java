package co.jinear.core.controller.material;

import co.jinear.core.manager.material.MaterialListingManager;
import co.jinear.core.model.request.material.MaterialTextSearchRequest;
import co.jinear.core.model.response.material.MaterialPaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/material/workspace/{workspaceId}/search")
public class MaterialSearchController {

    private final MaterialListingManager materialListingManager;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public MaterialPaginatedResponse searchMaterials(@PathVariable String workspaceId,
                                                     @Valid @RequestBody MaterialTextSearchRequest materialTextSearchRequest,
                                                     @RequestParam(required = false, defaultValue = "0") Integer page) {
        return materialListingManager.searchText(workspaceId, materialTextSearchRequest, page);
    }
}
