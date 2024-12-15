package co.jinear.core.service.project.domain;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.entity.project.ProjectDomain;
import co.jinear.core.model.enumtype.project.ProjectDomainCnameCheckResultType;
import co.jinear.core.model.enumtype.project.ProjectDomainType;
import co.jinear.core.repository.project.ProjectDomainRepository;
import co.jinear.core.service.caddymanager.CaddyManagerClient;
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
    private final CaddyManagerClient caddyManagerClient;
    private final ProjectDomainDnsChecker projectDomainDnsChecker;
    private final PassiveService passiveService;

    @Transactional
    public void findAvailableAndSync() {
        log.info("Find available and sync has started.");
        List<ProjectDomain> newAndReadyDomains = projectDomainRepository.findAvailableForCnameConfigDomains(ProjectDomainType.CUSTOM, ProjectDomainCnameCheckResultType.CNAME_CHECK_OK_READY_FOR_CADDY_MANAGER);
        if (!newAndReadyDomains.isEmpty()) {
            log.info("New and ready domains exists. size: {}", newAndReadyDomains.size());
            updateDomainsAsSetupCompleted(newAndReadyDomains);
            syncAll();
        }
    }

    public void reCheckFailedDomains() {
        log.info("Re-check failed domains has started.");
        findTimedOutSetupsAndUpdateAsCancelled();
        List<ProjectDomain> cnameCheckFailedList = projectDomainRepository.findAvailableForCnameConfigDomains(ProjectDomainType.CUSTOM, ProjectDomainCnameCheckResultType.CNAME_CHECK_FAILED);
        cnameCheckFailedList.stream()
                .filter(projectDomain -> projectDomainDnsChecker.matchesCname(projectDomain.getDomain()))
                .forEach(projectDomain -> {
                    log.info("Project domain cname check success. Updating as ready for caddy manager.");
                    projectDomain.setCnameCheckResult(ProjectDomainCnameCheckResultType.CNAME_CHECK_OK_READY_FOR_CADDY_MANAGER);
                    projectDomainRepository.save(projectDomain);
                });
    }

    private void findTimedOutSetupsAndUpdateAsCancelled() {
        String passiveId = passiveService.createSystemActionPassive();
        Date createdDateBefore = DateHelper.substractMinutes(DateHelper.now(), CNAME_SETUP_ALLOWED_WAIT_DURATION);
        log.info("Find timed out setups and update as cancelled has started. passiveId: {}, createdDateBefore: {}", passiveId, DateHelper.toMySQLDateFormat(createdDateBefore));
        projectDomainRepository.updateTimedOutDomains(ProjectDomainCnameCheckResultType.CNAME_CHECK_FAILED, ProjectDomainType.CUSTOM, createdDateBefore, passiveId, ProjectDomainCnameCheckResultType.CANCELLED_CNAME_NOT_SETUP_IN_TIME);
    }

    public void syncAll() {
        log.info("Sync all has started.");
        List<ProjectDomain> availableCustomDomains = projectDomainRepository.findAllByDomainTypeAndCnameCheckResultAndPassiveIdIsNullOrderByCreatedDateAsc(ProjectDomainType.CUSTOM, ProjectDomainCnameCheckResultType.SETUP_COMPLETED);
        if (availableCustomDomains.isEmpty()) {
            log.info("Skipping sync all. Available custom domains is empty");
            return;
        }
        List<String> domains = availableCustomDomains.stream().map(ProjectDomain::getDomain).toList();
        boolean result = caddyManagerClient.updateConfig(domains);
        log.info("Sync all has completed. result: {}", result ? "success" : "fail");
        if (!result) {
            throw new BusinessException("project.domain.sync-failed");
        }
    }

    private void updateDomainsAsSetupCompleted(List<ProjectDomain> newAndReadyDomains) {
        newAndReadyDomains.forEach(projectDomain -> projectDomain.setCnameCheckResult(ProjectDomainCnameCheckResultType.SETUP_COMPLETED));
        projectDomainRepository.saveAll(newAndReadyDomains);
    }
}
