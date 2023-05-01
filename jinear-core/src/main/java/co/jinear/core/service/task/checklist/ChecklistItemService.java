package co.jinear.core.service.task.checklist;

import co.jinear.core.converter.task.ChecklistItemDtoConverter;
import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.entity.task.ChecklistItem;
import co.jinear.core.model.vo.task.InitializeChecklistItemVo;
import co.jinear.core.repository.task.ChecklistItemRepository;
import co.jinear.core.service.passive.PassiveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final ChecklistItemDtoConverter checklistItemDtoConverter;
    private final PassiveService passiveService;

    public ChecklistItemDto initialize(InitializeChecklistItemVo initializeChecklistItemVo) {
        log.info("Initialize checklist item has started. initializeChecklistItemVo: {}", initializeChecklistItemVo);
        ChecklistItem checklistItem = mapInitialEntity(initializeChecklistItemVo);
        ChecklistItem saved = checklistItemRepository.save(checklistItem);
        return checklistItemDtoConverter.convert(saved);
    }

    @Transactional
    public void updateCheckState(String checklistItemId, boolean checked) {
        log.info("Update checklist item check status has started. checklistItemId: {}, checked: {}", checklistItemId, checked);
        checklistItemRepository.updateChecked(checklistItemId, checked);
    }

    @Transactional
    public void updateLabel(String checklistItemId, String label) {
        log.info("Update checklist item label has started. checklistItemId: {}, label: {}", checklistItemId, label);
        checklistItemRepository.updateLabel(checklistItemId, label);
    }

    @Transactional
    public String passivizeChecklistItem(String checklistItemId) {
        log.info("Passivize checklist item has started. checklistItemId: {}", checklistItemId);
        String passiveId = passiveService.createUserActionPassive();
        ChecklistItem checklistItem = retrieveEntity(checklistItemId);
        checklistItem.setPassiveId(passiveId);
        checklistItemRepository.save(checklistItem);
        return passiveId;
    }

    public ChecklistItemDto retrieve(String checklistItemId) {
        return checklistItemRepository.findByChecklistItemIdAndPassiveIdIsNull(checklistItemId)
                .map(checklistItemDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public ChecklistItemDto retrieveIncludingPassive(String checklistItemId) {
        return checklistItemRepository.findByChecklistItemId(checklistItemId)
                .map(checklistItemDtoConverter::convert)
                .orElseThrow(NotFoundException::new);
    }

    public ChecklistItem retrieveEntity(String checklistItemId) {
        return checklistItemRepository.findByChecklistItemIdAndPassiveIdIsNull(checklistItemId)
                .orElseThrow(NotFoundException::new);
    }

    private ChecklistItem mapInitialEntity(InitializeChecklistItemVo initializeChecklistItemVo) {
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setChecklistId(initializeChecklistItemVo.getChecklistId());
        checklistItem.setLabel(initializeChecklistItemVo.getLabel());
        checklistItem.setIsChecked(Boolean.FALSE);
        return checklistItem;
    }
}
