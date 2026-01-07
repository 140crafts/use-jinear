package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.entity.material.MaterialAccess;
import co.jinear.core.model.entity.media.Media;
import co.jinear.core.model.entity.workspace.Workspace;
import co.jinear.core.model.entity.workspace.WorkspaceMember;
import co.jinear.core.model.enumtype.material.MaterialAccessType;
import co.jinear.core.model.enumtype.material.MaterialType;
import co.jinear.core.model.enumtype.media.MediaFileUploadStatusType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.*;
import liquibase.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MaterialSearchCriteriaBuilder {

    public void addPassiveIdIsNull(CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        Predicate predicate = criteriaBuilder.isNull(root.<String>get(BaseEntity.Fields.passiveId));
        predicateList.add(predicate);
    }

    public void addMediaStatusAsCompleted(CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        Join mediaJoin = root.join(Material.Fields.media, jakarta.persistence.criteria.JoinType.LEFT);

        Predicate isFolder = criteriaBuilder.equal(root.get(Material.Fields.materialType), MaterialType.FOLDER);
        Predicate isCompleted = criteriaBuilder.equal(mediaJoin.get(Media.Fields.uploadStatus), MediaFileUploadStatusType.COMPLETED);

        predicateList.add(criteriaBuilder.or(isFolder, isCompleted));
    }

    public void addMediaContentTypeImageOrVideo(CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        Join<Material, Media> mediaJoin = root.join(Material.Fields.media, jakarta.persistence.criteria.JoinType.LEFT);

        Predicate isImage = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%image%");
        Predicate isVideo = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%video%");

        predicateList.add(criteriaBuilder.or(
                isImage, isVideo));
    }

    public void addMediaContentTypeDocument(CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        Join<Material, Media> mediaJoin = root.join(Material.Fields.media, jakarta.persistence.criteria.JoinType.LEFT);

        Predicate isPdf = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%pdf%");
        Predicate isWord = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%word%");
        Predicate isExcel = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%spreadsheet%");
        Predicate isPowerpoint = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%presentation%");
        Predicate isText = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%text%");
        Predicate isRtf = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%rtf%");
        Predicate isJson = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%json%");

        Predicate isOpenDocument = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%opendocument%");

        Predicate isAppleDoc = criteriaBuilder.like(criteriaBuilder.lower(mediaJoin.get(Media.Fields.contentType)), "%vnd.apple%");

        predicateList.add(criteriaBuilder.or(
                isPdf, isWord, isExcel, isPowerpoint, isText, isRtf, isJson, isOpenDocument, isAppleDoc
        ));
    }

    public void addAccessFilters(MaterialSearchVo materialSearchVo, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        String accountId = materialSearchVo.getAccountIdPerspective();
        if (StringUtils.isNotBlank(accountId)) {
            Predicate ownerIdPredicate = criteriaBuilder.equal(root.<String>get(Material.Fields.ownerId), accountId);
            Predicate accessTypeOwnerPredicate = criteriaBuilder.equal(root.<String>get(Material.Fields.materialAccessType), MaterialAccessType.OWNER_ONLY);
            Predicate ownerOnlyPredicate = criteriaBuilder.and(ownerIdPredicate, accessTypeOwnerPredicate);

            Join<Material, Workspace> workspaceJoin = root.join(Material.Fields.workspace, JoinType.LEFT);
            Join<Workspace, WorkspaceMember> workspaceMemberJoin = workspaceJoin.join(Workspace.Fields.workspaceMembers, JoinType.LEFT);
            Predicate workspaceMemberPassiveIdIsNull = criteriaBuilder.isNull(workspaceMemberJoin.get(BaseEntity.Fields.passiveId));
            Predicate workspaceMemberGivenAccountIdPredicate = criteriaBuilder.equal(workspaceMemberJoin.get(WorkspaceMember.Fields.accountId), accountId);
            Predicate accessTypeWorkspaceMembersPredicate = criteriaBuilder.equal(root.<String>get(Material.Fields.materialAccessType), MaterialAccessType.WORKSPACE_MEMBERS);
            Predicate workspaceMembersOnlyPredicate = criteriaBuilder.and(workspaceMemberPassiveIdIsNull, workspaceMemberGivenAccountIdPredicate, accessTypeWorkspaceMembersPredicate);

            Join<Material, MaterialAccess> materialAccessJoin = root.join(Material.Fields.materialAccesses, jakarta.persistence.criteria.JoinType.LEFT);
            Predicate accessTypeGrainedPredicate = criteriaBuilder.equal(root.<String>get(Material.Fields.materialAccessType), MaterialAccessType.GRAINED);
            Predicate materialAccessPassiveIdIsNull = criteriaBuilder.isNull(materialAccessJoin.get(BaseEntity.Fields.passiveId));
            Predicate materialAccessGivenAccountIdPredicate = criteriaBuilder.equal(materialAccessJoin.get(MaterialAccess.Fields.accountId), accountId);
            Predicate grainedAccessPredicate = criteriaBuilder.and(accessTypeGrainedPredicate, materialAccessPassiveIdIsNull, materialAccessGivenAccountIdPredicate);
            Predicate materialOwnerIsGivenAccountIdPredicate = criteriaBuilder.equal(root.get(Material.Fields.ownerId), accountId);
            Predicate grainedAccessOrOwnerIsGivenAccountIdPredicate = criteriaBuilder.or(grainedAccessPredicate, materialOwnerIsGivenAccountIdPredicate);

            Predicate anyoneWithLinkPredicate = criteriaBuilder.equal(root.<String>get(Material.Fields.materialAccessType), MaterialAccessType.ANYONE_WITH_LINK);

            Predicate accessPredicate = criteriaBuilder.or(ownerOnlyPredicate, workspaceMembersOnlyPredicate, grainedAccessOrOwnerIsGivenAccountIdPredicate, anyoneWithLinkPredicate);
            predicateList.add(accessPredicate);
        }
    }

    public void addFolderOrMediaVisibilityType(MediaVisibilityType visibilityType, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (Objects.nonNull(visibilityType)) {
            Join<Material, Media> mediaJoin = root.join(Material.Fields.media, jakarta.persistence.criteria.JoinType.LEFT);
            Predicate hasVisibilityType = criteriaBuilder.equal(mediaJoin.get(Media.Fields.visibility), visibilityType);
            predicateList.add(criteriaBuilder.or(
                    hasVisibilityType));
        }
    }

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Material.Fields.workspaceId), workspaceId);
            predicateList.add(predicate);
        }
    }

    public void addMaterialType(MaterialType materialType, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (Objects.nonNull(materialType)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Material.Fields.materialType), materialType);
            predicateList.add(predicate);
        }
    }

    public void addParentMaterialId(String parentMaterialId, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        Predicate predicate = Objects.nonNull(parentMaterialId) ?
                criteriaBuilder.equal(root.<String>get(Material.Fields.parentMaterialId), parentMaterialId) :
                criteriaBuilder.isNull(root.<String>get(Material.Fields.parentMaterialId));
        predicateList.add(predicate);
    }
}
