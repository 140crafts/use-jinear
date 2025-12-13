package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.material.Material;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Material.Fields.workspaceId), workspaceId);
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
