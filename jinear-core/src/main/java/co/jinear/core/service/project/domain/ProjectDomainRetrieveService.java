package co.jinear.core.service.project.domain;

import co.jinear.core.converter.project.ProjectDomainDtoConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.ProjectDomainDto;
import co.jinear.core.model.entity.project.ProjectDomain;
import co.jinear.core.repository.project.ProjectDomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainRetrieveService {

    private final ProjectDomainRepository projectDomainRepository;
    private final ProjectDomainDtoConverter projectDomainDtoConverter;

    public ProjectDomainDto retrieve(String projectDomainId) {
        log.info("Retrieve project domain has started. projectDomainId: {}", projectDomainId);
        return projectDomainRepository.findByProjectDomainIdAndPassiveIdIsNull(projectDomainId)
                .map(projectDomainDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public ProjectDomainDto retrieveByDomain(String domain) {
        log.info("Retrieve project domain has started. domain: {}", domain);
        return projectDomainRepository.findByDomainAndPassiveIdIsNull(domain)
                .map(projectDomainDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public ProjectDomain retrieveEntity(String projectDomainId) {
        log.info("Retrieve project domain entity has started. projectDomainId: {}", projectDomainId);
        return projectDomainRepository.findByProjectDomainIdAndPassiveIdIsNull(projectDomainId)
                .orElseThrow(NotFoundException::new);
    }

    public void validateDomainIsNotInUse(String domain) {
        log.info("Validate domain is not in use has started. domain: {}", domain);
        if (projectDomainRepository.existsByDomainAndPassiveIdIsNull(domain)) {
            throw new BusinessException("project.domain.already-in-use");
        }
    }
}
