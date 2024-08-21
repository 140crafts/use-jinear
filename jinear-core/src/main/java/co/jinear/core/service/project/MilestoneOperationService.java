package co.jinear.core.service.project;

import co.jinear.core.converter.project.InitializeMilestoneVoToEntityConverter;
import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.richtext.RichTextDto;
import co.jinear.core.model.entity.project.Milestone;
import co.jinear.core.model.enumtype.richtext.RichTextType;
import co.jinear.core.model.vo.project.InitializeMilestoneVo;
import co.jinear.core.model.vo.richtext.InitializeRichTextVo;
import co.jinear.core.repository.project.MilestoneRepository;
import co.jinear.core.service.richtext.RichTextInitializeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilestoneOperationService {

    private final MilestoneRepository milestoneRepository;
    private final InitializeMilestoneVoToEntityConverter initializeMilestoneVoToEntityConverter;
    private final RichTextInitializeService richTextInitializeService;
    private final MilestoneRetrieveService milestoneRetrieveService;

    public void initialize(InitializeMilestoneVo initializeMilestoneVo) {
        log.info("Initialize milestone has started. initializeMilestoneVo: {}", initializeMilestoneVo);
        Milestone saved = mapAndSaveEntity(initializeMilestoneVo);
        initializeAndAssignDescription(initializeMilestoneVo, saved);
    }

    public void updateTitle(String milestoneId, String title) {
        validateTitleIsNotEmpty(title);
        log.info("Update title has started. milestoneId: {}, title: {}", milestoneId, title);
        Milestone milestone = milestoneRetrieveService.retrieveEntity(milestoneId);
        milestone.setTitle(title);
        milestoneRepository.save(milestone);
    }

    @Transactional
    public void updateDescription(String milestoneId, String description) {
        log.info("Update title has started. milestoneId: {}, description: {}", milestoneId, description);
        Milestone milestone = milestoneRetrieveService.retrieveEntity(milestoneId);
        InitializeRichTextVo initializeRichTextVo = mapDescriptionInitVo(milestoneId, description);
        RichTextDto richTextDto = richTextInitializeService.initializeRichText(initializeRichTextVo);
        milestone.setDescriptionRichTextId(richTextDto.getRichTextId());
        milestoneRepository.save(milestone);
    }

    public void updateTargetDate(String milestoneId, ZonedDateTime targetDate) {
        log.info("Update target date has started. milestoneId: {}, targetDate: {}", milestoneId, targetDate);
        Milestone milestone = milestoneRetrieveService.retrieveEntity(milestoneId);
        milestone.setTargetDate(targetDate);
        milestoneRepository.save(milestone);
    }

    @Transactional
    public void updateOrder(String milestoneId, Integer nextOrder) {
        validateNextOrderIsNotNull(nextOrder);
        Milestone milestone = milestoneRetrieveService.retrieveEntity(milestoneId);
        Integer currentOrder = milestone.getMilestoneOrder();
        log.info("Update milestone order has started. milestoneId: {}, currentOrder: {}, nextOrder: {}", milestoneId, currentOrder, nextOrder);
        final String projectId = milestone.getProjectId();
        int comparisonResult = currentOrder.compareTo(nextOrder);
        log.info("Milestone order comparison result: {}", comparisonResult);
        switch (comparisonResult) {
            case -1 -> milestoneRepository.updateOrderDownward(projectId, currentOrder, nextOrder);
            case 1 -> milestoneRepository.updateOrderUpward(projectId, currentOrder, nextOrder);
            default -> log.info("Milestone order is same.");
        }
        milestone.setMilestoneOrder(nextOrder);
        milestoneRepository.save(milestone);
    }

    private Milestone mapAndSaveEntity(InitializeMilestoneVo initializeMilestoneVo) {
        Milestone milestone = initializeMilestoneVoToEntityConverter.convert(initializeMilestoneVo);
        return milestoneRepository.save(milestone);
    }

    private void initializeAndAssignDescription(InitializeMilestoneVo initializeMilestoneVo, Milestone saved) {
        if (StringHelper.isNotEmpty(initializeMilestoneVo.getDescription())) {
            InitializeRichTextVo initializeRichTextVo = mapDescriptionInitVo(saved.getMilestoneId(), initializeMilestoneVo.getDescription());
            RichTextDto description = richTextInitializeService.initializeRichText(initializeRichTextVo);
            saved.setDescriptionRichTextId(description.getRichTextId());
        }
    }

    private InitializeRichTextVo mapDescriptionInitVo(String milestoneId, String description) {
        InitializeRichTextVo initializeRichTextVo = new InitializeRichTextVo();
        initializeRichTextVo.setRelatedObjectId(milestoneId);
        initializeRichTextVo.setValue(description);
        initializeRichTextVo.setType(RichTextType.PROJECT_MILESTONE_DESCRIPTION);
        return initializeRichTextVo;
    }

    private void validateTitleIsNotEmpty(String title) {
        if (StringHelper.isEmpty(title)) {
            throw new BusinessException("project.milestone.title");
        }
    }

    private void validateNextOrderIsNotNull(Integer nextOrder) {
        if (Objects.isNull(nextOrder)) {
            throw new BusinessException();
        }
    }
}
