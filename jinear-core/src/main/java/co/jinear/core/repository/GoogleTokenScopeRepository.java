package co.jinear.core.repository;

import co.jinear.core.model.entity.google.GoogleTokenScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoogleTokenScopeRepository extends JpaRepository<GoogleTokenScope, String> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update GoogleTokenScope googleTokenScope
                set googleTokenScope.passiveId=:passiveId
                    where 
                        googleTokenScope.googleTokenId=:googleTokenId and 
                        googleTokenScope.passiveId is null
                """)
    void updateAllAsPassiveWithGoogleTokenId(@Param("googleTokenId") String googleTokenId, @Param("passiveId") String passiveId);

}
