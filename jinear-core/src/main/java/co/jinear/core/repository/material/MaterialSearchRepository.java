package co.jinear.core.repository.material;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.material.Material;
import co.jinear.core.model.enumtype.material.MaterialSearchContentFilterType;
import co.jinear.core.model.enumtype.material.MaterialSearchSortType;
import co.jinear.core.model.enumtype.media.MediaVisibilityType;
import co.jinear.core.model.vo.material.MaterialSearchVo;
import co.jinear.core.repository.criteriabuilder.MaterialSearchCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MaterialSearchRepository {

    private static final int PAGE_SIZE = 500;
    private final EntityManager entityManager;
    private final MaterialSearchCriteriaBuilder materialSearchCriteriaBuilder;

    public Page<Material> search(MaterialSearchVo materialSearchVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Material> criteriaQuery = criteriaBuilder.createQuery(Material.class);
        Root<Material> materialRoot = criteriaQuery.from(Material.class);
        List<Predicate> predicateList = retrievePredicates(materialSearchVo, criteriaBuilder, materialRoot);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Material> countRoot = countQuery.from(Material.class);
        countQuery.select(criteriaBuilder.countDistinct(countRoot));

        List<Predicate> countPredicateList = retrievePredicates(materialSearchVo, criteriaBuilder, countRoot);

        return executeAndRetrievePageableResults(
                criteriaBuilder,
                criteriaQuery,
                countQuery,
                materialRoot,
                predicateList,
                countPredicateList,
                materialSearchVo.getMaterialSearchSortType(),
                PageRequest.of(materialSearchVo.getPage(), PAGE_SIZE));
    }

    private List<Predicate> retrievePredicates(MaterialSearchVo materialSearchVo, CriteriaBuilder criteriaBuilder, Root<Material> root) {
        List<Predicate> predicateList = Lists.newArrayList();
        materialSearchCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, root, predicateList);
        materialSearchCriteriaBuilder.addMediaStatusAsCompleted(criteriaBuilder, root, predicateList);
        materialSearchCriteriaBuilder.addWorkspaceId(materialSearchVo.getWorkspaceId(), criteriaBuilder, root, predicateList);
        materialSearchCriteriaBuilder.addMaterialType(materialSearchVo.getMaterialType(), criteriaBuilder, root, predicateList);
        addParentIdFilterIfNoContentFilterSelected(materialSearchVo, criteriaBuilder, root, predicateList);
        addContentFilter(materialSearchVo, criteriaBuilder, root, predicateList);
        return predicateList;
    }

    private void addParentIdFilterIfNoContentFilterSelected(MaterialSearchVo materialSearchVo, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (Objects.isNull(materialSearchVo.getMaterialSearchContentFilterType())) {
            materialSearchCriteriaBuilder.addParentMaterialId(materialSearchVo.getParentMaterialId(), criteriaBuilder, root, predicateList);
        }
    }

    private void addContentFilter(MaterialSearchVo materialSearchVo, CriteriaBuilder criteriaBuilder, Root<Material> root, List<Predicate> predicateList) {
        if (MaterialSearchContentFilterType.DOC.equals(materialSearchVo.getMaterialSearchContentFilterType())) {
            materialSearchCriteriaBuilder.addMediaContentTypeDocument(criteriaBuilder, root, predicateList);
        } else if (MaterialSearchContentFilterType.IMAGE.equals(materialSearchVo.getMaterialSearchContentFilterType())) {
            materialSearchCriteriaBuilder.addMediaContentTypeImageOrVideo(criteriaBuilder, root, predicateList);
        } else if (MaterialSearchContentFilterType.SHARED.equals(materialSearchVo.getMaterialSearchContentFilterType())) {
            materialSearchCriteriaBuilder.addFolderOrMediaVisibilityType(MediaVisibilityType.PUBLIC, criteriaBuilder, root, predicateList);
        }
    }

    @SuppressWarnings("java:S107")
    private Page<Material> executeAndRetrievePageableResults(CriteriaBuilder criteriaBuilder,
                                                             CriteriaQuery<Material> criteriaQuery,
                                                             CriteriaQuery<Long> countQuery,
                                                             Root<Material> root,
                                                             List<Predicate> searchPredicate,
                                                             List<Predicate> countPredicate,
                                                             MaterialSearchSortType filterSort,
                                                             Pageable pageable) {

        criteriaQuery.where(searchPredicate.toArray(new Predicate[searchPredicate.size()]));
        setOrder(criteriaBuilder, criteriaQuery, root, filterSort);

        List<Material> result = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        countQuery.where(countPredicate.toArray(new Predicate[searchPredicate.size()]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(result, pageable, count);
    }

    private void setOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<Material> criteriaQuery, Root<Material> root, MaterialSearchSortType searchSortType) {
        Order materialTypeOrder = criteriaBuilder.desc(root.get(Material.Fields.materialType));
        if (searchSortType != null) {
            Order secondaryOrder = switch (searchSortType) {
                case IDATE_ASC -> criteriaBuilder.asc(root.get(BaseEntity.Fields.createdDate));
                case IDATE_DESC -> criteriaBuilder.desc(root.get(BaseEntity.Fields.createdDate));
                case UDATE_ASC -> criteriaBuilder.asc(root.get(BaseEntity.Fields.lastUpdatedDate));
                case UDATE_DESC -> criteriaBuilder.desc(root.get(BaseEntity.Fields.lastUpdatedDate));
                case NAME_ASC -> criteriaBuilder.asc(root.get(Material.Fields.name));
                case NAME_DESC -> criteriaBuilder.desc(root.get(Material.Fields.name));
                case SIZE_ASC, SIZE_DESC -> null;
            };

            if (secondaryOrder != null) {
                criteriaQuery.orderBy(materialTypeOrder, secondaryOrder);
            } else {
                criteriaQuery.orderBy(materialTypeOrder);
            }
        } else {
            criteriaQuery.orderBy(materialTypeOrder);
        }
    }
}
