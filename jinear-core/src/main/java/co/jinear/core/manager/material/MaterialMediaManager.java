package co.jinear.core.manager.material;

import co.jinear.core.model.dto.material.MaterialDto;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.service.SessionInfoService;
import co.jinear.core.service.material.MaterialAccessValidationService;
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
    private final MaterialAccessValidationService materialAccessValidationService;

    public void downloadMaterialMedia(HttpServletResponse response, String materialId) throws IOException {
        String currentAccountId = sessionInfoService.currentAccountIdInclAnonymous();
        MaterialDto materialDto = materialRetrieveService.retrieve(materialId, MaterialType.FILE);
        validateMaterialAccessIfMaterialIsNotPublic(materialDto, currentAccountId);
        log.info("Download material media has started. currentAccountId: {}", currentAccountId);
        URL redirectUrl = mediaRetrieveService.retrievePresignedPublicDownloadLink(materialDto.getMedia());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(materialDto.getName()));
        response.sendRedirect(redirectUrl.toString());
    }

    private void validateMaterialAccessIfMaterialIsNotPublic(MaterialDto materialDto, String currentAccountId) {
        if (!MaterialAccessType.ANYONE_WITH_LINK.equals(materialDto.getMaterialAccessType())) {
            workspaceValidator.validateHasAccess(currentAccountId, materialDto.getWorkspaceId());
            materialAccessValidationService.validateMaterialReadAccess(currentAccountId, materialDto);
        }
    }
}
