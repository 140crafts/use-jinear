package co.jinear.core.repository;

import co.jinear.core.model.entity.google.GmailThread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GmailThreadRepository extends JpaRepository<GmailThread, String> {

    Page<GmailThread> findAllByGoogleTokenIdAndPassiveIdIsNullOrderByLastUpdatedDateDesc(String googleTokenId, Pageable pageable);

    @Query("from GmailThread gmailThread where gmailThread.gId in (:googleTokenIdList) and gmailThread.passiveId is null")
    List<GmailThread> findAllByGIdInAndPassiveIdIsNull(@Param("googleTokenIdList") List<String> googleTokenIdList);
}
