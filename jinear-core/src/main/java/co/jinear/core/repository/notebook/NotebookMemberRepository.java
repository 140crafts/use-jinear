package co.jinear.core.repository.notebook;

import co.jinear.core.model.entity.note.NotebookMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotebookMemberRepository extends JpaRepository<NotebookMember, String> {

    Optional<NotebookMember> findFirstByAccountIdAndNotebookIdAndPassiveIdIsNull(String accountId, String notebookId);

    boolean existsByAccountIdAndNotebookIdAndPassiveIdIsNull(String accountId, String notebookId);

    List<NotebookMember> findAllByAccountIdAndWorkspaceIdAndPassiveIdIsNull(String accountId, String workspaceId);

    Page<NotebookMember> findAllByNotebookIdAndPassiveIdIsNull(String notebookId, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update NotebookMember notebookMember
                set notebookMember.passiveId = :passiveId
                    where
                        notebookMember.notebookId = :notebookId and
                        notebookMember.passiveId is null
                """)
    void removeAllMembers(@Param("notebookId") String notebookId, @Param("passiveId") String passiveId);
}
