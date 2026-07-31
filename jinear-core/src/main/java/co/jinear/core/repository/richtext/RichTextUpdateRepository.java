package co.jinear.core.repository.richtext;

import co.jinear.core.model.entity.richtext.RichTextUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RichTextUpdateRepository extends JpaRepository<RichTextUpdate, String> {

    List<RichTextUpdate> findByRichTextIdAndSeqGreaterThanOrderBySeqAsc(String richTextId, long since);

    @Modifying
    @Query("delete from RichTextUpdate u where u.richTextId = :richTextId and u.seq <= :seq")
    void deleteByRichTextIdAndSeqLessThanEqual(@Param("richTextId") String richTextId, @Param("seq") long seq);
}
