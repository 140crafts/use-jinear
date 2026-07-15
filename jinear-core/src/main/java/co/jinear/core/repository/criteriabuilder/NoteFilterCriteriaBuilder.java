package co.jinear.core.repository.criteriabuilder;

import co.jinear.core.model.entity.BaseEntity;
import co.jinear.core.model.entity.note.Note;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class NoteFilterCriteriaBuilder {

    public void addPassiveIdIsNull(CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        Predicate predicate = criteriaBuilder.isNull(root.<String>get(BaseEntity.Fields.passiveId));
        predicateList.add(predicate);
    }

    public void addWorkspaceId(String workspaceId, CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        if (Objects.nonNull(workspaceId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Note.Fields.workspaceId), workspaceId);
            predicateList.add(predicate);
        }
    }

    public void addNotebookId(String notebookId, CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        if (Objects.nonNull(notebookId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Note.Fields.notebookId), notebookId);
            predicateList.add(predicate);
        }
    }

    public void addParentNoteId(String parentNoteId, CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        if (Objects.nonNull(parentNoteId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Note.Fields.parentNoteId), parentNoteId);
            predicateList.add(predicate);
        }
    }

    public void addNoteId(String noteId, CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        if (Objects.nonNull(noteId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Note.Fields.noteId), noteId);
            predicateList.add(predicate);
        }
    }

    public void addOwnerId(String ownerId, CriteriaBuilder criteriaBuilder, Root<Note> root, List<Predicate> predicateList) {
        if (Objects.nonNull(ownerId)) {
            Predicate predicate = criteriaBuilder.equal(root.<String>get(Note.Fields.ownerId), ownerId);
            predicateList.add(predicate);
        }
    }
}
