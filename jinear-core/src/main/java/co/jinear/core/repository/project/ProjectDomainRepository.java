package co.jinear.core.repository.project;

import co.jinear.core.model.entity.project.ProjectDomain;
import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectDomainRepository extends JpaRepository<ProjectDomain, String> {

    Optional<ProjectDomain> findByProjectDomainIdAndPassiveIdIsNull(String projectDomainId);

    Optional<ProjectDomain> findByDomainAndPassiveIdIsNull(String domain);

    boolean existsByDomainAndPassiveIdIsNull(String domain);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ProjectDomain pd
                set
                 pd.passiveId = :passiveId,
                 pd.cnameCheckResult = :toCnameCheckResult,
                 pd.lastUpdatedDate = current_timestamp()
                where
                     pd.cnameCheckResult = :whichCnameCheckResult and
                     pd.domainType = :projectDomainType and
                     pd.createdDate < :createdDateBefore and
                     pd.passiveId is null
                """)
    void updateTimedOutDomains(@Param("whichCnameCheckResult") ProjectDomainCnameCheckResultType whichCnameCheckResult,
                               @Param("projectDomainType") ProjectDomainType projectDomainType,
                               @Param("createdDateBefore") Date createdDateBefore,
                               @Param("passiveId") String passiveId,
                               @Param("toCnameCheckResult") ProjectDomainCnameCheckResultType toCnameCheckResult);


    @Query("""
            from ProjectDomain pd where
            pd.passiveId is null and
            pd.domainType = :projectDomainType and
            pd.cnameCheckResult = :cnameCheckResult
            """)
    List<ProjectDomain> findAvailableForCnameConfigDomains(@Param("projectDomainType") ProjectDomainType projectDomainType,
                                                           @Param("cnameCheckResult") ProjectDomainCnameCheckResultType cnameCheckResult);

    List<ProjectDomain> findAllByDomainTypeAndCnameCheckResultAndPassiveIdIsNullOrderByCreatedDateAsc(ProjectDomainType projectDomainType, ProjectDomainCnameCheckResultType cnameCheckResult);
}
