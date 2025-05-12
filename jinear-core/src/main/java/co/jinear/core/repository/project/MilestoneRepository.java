package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MilestoneRepository extends JpaRepository<Milestone, String> {

    Optional<Milestone> findByMilestoneIdAndPassiveIdIsNull(String milestoneId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Milestone milestone
                set milestone.milestoneOrder = milestone.milestoneOrder + 1
                    where
                        milestone.projectId = :projectId and
                        (:newOrder <= milestone.milestoneOrder and milestone.milestoneOrder < :currentOrder) and
                        milestone.passiveId is null
                """)
    void updateOrderUpward(@Param("projectId") String projectId,
                           @Param("currentOrder") int currentOrder,
                           @Param("newOrder") int newOrder);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Milestone milestone
                set milestone.milestoneOrder = milestone.milestoneOrder - 1
                    where
                        milestone.projectId = :projectId and
                        ( milestone.milestoneOrder > :currentOrder and milestone.milestoneOrder <= :newOrder) and
                        milestone.passiveId is null
                """)
    void updateOrderDownward(@Param("projectId") String projectId,
                             @Param("currentOrder") int currentOrder,
                             @Param("newOrder") int newOrder);

    boolean existsByProjectIdAndMilestoneIdAndPassiveIdIsNull(String projectId, String milestoneId);

    List<Milestone> findAllByProjectIdIsInAndPassiveIdIsNull(List<String> projectIds);
}
