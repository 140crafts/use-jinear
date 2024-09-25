package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectFeedSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectFeedSettingsRepository extends JpaRepository<ProjectFeedSettings, String> {

    Optional<ProjectFeedSettings> findByProjectIdAndPassiveIdIsNull(String projectId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProjectFeedSettings projectFeedSettings
                set projectFeedSettings.accessKey=:accessKey
                    where
                        projectFeedSettings.projectId = :projectId and
                        projectFeedSettings.project.passiveId is null
                """)
    void updateAccessKey(@Param("projectId") String projectId, @Param("accessKey") String accessKey);

}
