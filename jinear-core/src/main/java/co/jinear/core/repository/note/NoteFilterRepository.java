package co.jinear.core.repository.note;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.note.Note;
import co.jinear.core.model.vo.note.NoteFilterVo;
import co.jinear.core.repository.criteriabuilder.NoteFilterCriteriaBuilder;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoteFilterRepository {

    private static final int PAGE_SIZE = 500;
    private final EntityManager entityManager;
    private final NoteFilterCriteriaBuilder noteFilterCriteriaBuilder;

    public Page<Note> filter(NoteFilterVo noteFilterVo) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> criteriaQuery = criteriaBuilder.createQuery(Note.class);
        Root<Note> noteRoot = criteriaQuery.from(Note.class);
        List<Predicate> predicateList = retrievePredicates(noteFilterVo, criteriaBuilder, noteRoot);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Note> countRoot = countQuery.from(Note.class);
        countQuery.select(criteriaBuilder.countDistinct(countRoot));

        List<Predicate> countPredicateList = retrievePredicates(noteFilterVo, criteriaBuilder, countRoot);
        return executeAndRetrievePageableResults(
                criteriaBuilder,
                criteriaQuery,
                countQuery,
                noteRoot,
                predicateList,
                countPredicateList,
                PageRequest.of(noteFilterVo.getPage(), PAGE_SIZE));
    }

    private List<Predicate> retrievePredicates(NoteFilterVo noteFilterVo, CriteriaBuilder criteriaBuilder, Root<Note> noteRoot) {
        List<Predicate> predicateList = Lists.newArrayList();
        noteFilterCriteriaBuilder.addPassiveIdIsNull(criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addWorkspaceId(noteFilterVo.getWorkspaceId(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addNotebookId(noteFilterVo.getNotebookId(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addNotebookIdIsNull(noteFilterVo.isNotebookIdIsNull(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addParentNoteId(noteFilterVo.getParentNoteId(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addParentNoteIdIsNull(noteFilterVo.isParentNoteIdIsNull(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addNoteId(noteFilterVo.getNoteId(), criteriaBuilder, noteRoot, predicateList);
        noteFilterCriteriaBuilder.addOwnerId(noteFilterVo.getOwnerId(), criteriaBuilder, noteRoot, predicateList);
        return predicateList;
    }

    private Page<Note> executeAndRetrievePageableResults(CriteriaBuilder criteriaBuilder,
                                                         CriteriaQuery<Note> criteriaQuery,
                                                         CriteriaQuery<Long> countQuery,
                                                         Root<Note> root,
                                                         List<Predicate> searchPredicate,
                                                         List<Predicate> countPredicate,
                                                         Pageable pageable) {

        criteriaQuery.where(searchPredicate.toArray(new Predicate[searchPredicate.size()]));
        criteriaBuilder.asc(root.get(BaseEntity.Fields.createdDate));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(BaseEntity.Fields.createdDate)));

        List<Note> result = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        countQuery.where(countPredicate.toArray(new Predicate[countPredicate.size()]));
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(result, pageable, count);
    }
}
