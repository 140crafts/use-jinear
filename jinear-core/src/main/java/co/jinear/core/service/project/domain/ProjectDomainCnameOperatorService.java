package co.jinear.core.service.project.domain;

import co.jinear.core.model.entity.project.ProjectDomain;
import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import co.jinear.core.repository.project.ProjectDomainRepository;
import co.jinear.core.service.passive.PassiveService;
import co.jinear.core.system.util.DateHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainCnameOperatorService {

    private static final int CNAME_SETUP_ALLOWED_WAIT_DURATION = 30;

    private final ProjectDomainRepository projectDomainRepository;
    private final ProjectDomainDnsChecker projectDomainDnsChecker;
    private final PassiveService passiveService;

    @Transactional
    public void reCheckFailedDomains() {
        log.info("Re-check failed domains has started.");
        findTimedOutSetupsAndUpdateAsCancelled();
        List<ProjectDomain> cnameCheckFailedList = projectDomainRepository.findAvailableForCnameConfigDomains(ProjectDomainType.CUSTOM, ProjectDomainCnameCheckResultType.CNAME_CHECK_FAILED);
        cnameCheckFailedList.stream()
                .filter(projectDomain -> projectDomainDnsChecker.matchesCname(projectDomain.getDomain()))
                .forEach(projectDomain -> {
                    log.info("Project domain cname check success. Updating as ready for caddy manager.");
                    projectDomain.setCnameCheckResult(ProjectDomainCnameCheckResultType.SETUP_COMPLETED);
                    projectDomainRepository.save(projectDomain);
                });
    }

    private void findTimedOutSetupsAndUpdateAsCancelled() {
        Date createdDateBefore = DateHelper.substractMinutes(DateHelper.now(), CNAME_SETUP_ALLOWED_WAIT_DURATION);
        boolean anyTimedOutDomainExists = projectDomainRepository.existsByCnameCheckResultAndDomainTypeAndCreatedDateBeforeAndPassiveIdIsNull(ProjectDomainCnameCheckResultType.CNAME_CHECK_FAILED, ProjectDomainType.CUSTOM, createdDateBefore);
        if (anyTimedOutDomainExists) {
            log.info("Timed out domains found.");
            String passiveId = passiveService.createSystemActionPassive();
            log.info("Find timed out setups and update as cancelled has started. passiveId: {}, createdDateBefore: {}", passiveId, DateHelper.toMySQLDateFormat(createdDateBefore));
            projectDomainRepository.updateTimedOutDomains(ProjectDomainCnameCheckResultType.CNAME_CHECK_FAILED, ProjectDomainType.CUSTOM, createdDateBefore, passiveId, ProjectDomainCnameCheckResultType.CANCELLED_CNAME_NOT_SETUP_IN_TIME);
        }
    }
}
