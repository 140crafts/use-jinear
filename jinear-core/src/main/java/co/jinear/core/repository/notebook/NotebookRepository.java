package co.jinear.core.repository.notebook;

import co.jinear.core.model.entity.note.Notebook;
import co.jinear.core.model.enumtype.notebook.NotebookVisibilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotebookRepository extends JpaRepository<Notebook, String> {

    Optional<Notebook> findByNotebookIdAndPassiveIdIsNull(String notebookId);

    boolean existsByNotebookIdAndWorkspaceIdAndPassiveIdIsNull(String notebookId, String workspaceId);

    Page<Notebook> findAllByWorkspaceIdAndPassiveIdIsNullOrderByCreatedDateDesc(String workspaceId, Pageable pageable);

    @Query("""
            select notebook from Notebook notebook
                where notebook.workspaceId = :workspaceId
                    and notebook.passiveId is null
                    and (
                        notebook.ownerId = :accountId
                        or notebook.visibility = :publicVisibility
                        or exists (
                            select 1 from NotebookMember member
                                where member.notebookId = notebook.notebookId
                                    and member.accountId = :accountId
                                    and member.passiveId is null
                        )
                    )
                order by notebook.createdDate desc
            """)
    Page<Notebook> findAccessibleNotebooks(@Param("workspaceId") String workspaceId,
                                           @Param("accountId") String accountId,
                                           @Param("publicVisibility") NotebookVisibilityType publicVisibility,
                                           Pageable pageable);
}
