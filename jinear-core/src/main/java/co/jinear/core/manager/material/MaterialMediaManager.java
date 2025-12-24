package co.jinear.core.manager.material;

import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.response.BaseResponse;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialRetrieveService;
import co.jinear.core.service.media.MediaOperationService;
import co.jinear.core.service.media.MediaRetrieveService;
import co.jinear.core.validator.workspace.WorkspaceValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialMediaManager {

    private final SessionInfoService sessionInfoService;
    private final MaterialRetrieveService materialRetrieveService;
    private final WorkspaceValidator workspaceValidator;
    private final MediaRetrieveService mediaRetrieveService;
    private final MediaOperationService mediaOperationService;

    public void downloadMaterialMedia(HttpServletResponse response, String materialId) throws IOException {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId, MaterialType.FILE);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        log.info("Download material media has started. currentAccountId: {}", currentAccountId);
        URL redirectUrl = mediaRetrieveService.retrievePresignedPublicDownloadLink(materialDto.getMedia());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(materialDto.getName()));
        response.sendRedirect(redirectUrl.toString());
    }

    public BaseResponse changeRelatedMediaAccess(String materialId, MediaVisibilityType mediaVisibilityType) {
        String currentAccountId = sessionInfoService.currentAccountId();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId, MaterialType.FILE);
        workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
        log.info("Change related media access has started. currentAccountId: {}, mediaVisibilityType: {}", currentAccountId, mediaVisibilityType);
        mediaOperationService.updateMediaVisibility(materialDto.getMediaId(), mediaVisibilityType);
        return new BaseResponse();
    }
}
