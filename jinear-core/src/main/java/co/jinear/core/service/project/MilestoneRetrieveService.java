package co.jinear.core.service.project;

import co.jinear.core.converter.project.MilestoneDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.project.MilestoneDto;
import co.jinear.core.model.entity.project.Milestone;
import co.jinear.core.repository.project.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilestoneRetrieveService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneDtoConverter milestoneDtoConverter;

    public Milestone retrieveEntity(String milestoneId) {
        log.info("Retrieve milestone entity has started. milestoneId: {}", milestoneId);
        return milestoneRepository.findByMilestoneIdAndPassiveIdIsNull(milestoneId)
                .orElseThrow(NotFoundException::new);
    }

    public MilestoneDto retrieve(String milestoneId) {
        log.info("Retrieve milestone has started. milestoneId: {}", milestoneId);
        return milestoneRepository.findByMilestoneIdAndPassiveIdIsNull(milestoneId)
                .map(milestoneDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public Optional<MilestoneDto> retrieveOptional(String milestoneId) {
        log.info("Retrieve milestone optional has started. milestoneId: {}", milestoneId);
        return milestoneRepository.findByMilestoneIdAndPassiveIdIsNull(milestoneId)
                .map(milestoneDtoConverter::convert);
    }

    public List<MilestoneDto> retrieveAllByProjectIds(List<String> projectIds) {
        return milestoneRepository.findAllByProjectIdIsInAndPassiveIdIsNull(projectIds)
                .stream()
                .map(milestoneDtoConverter::convert)
                .toList();
    }

    public boolean checkExistsByProjectIdAndMilestoneId(String projectId, String milestoneId) {
        log.info("Check exists by project id and milestone id has started. projectId: {}, milestoneId: {}", projectId, milestoneId);
        return milestoneRepository.existsByProjectIdAndMilestoneIdAndPassiveIdIsNull(projectId, milestoneId);
    }
}
