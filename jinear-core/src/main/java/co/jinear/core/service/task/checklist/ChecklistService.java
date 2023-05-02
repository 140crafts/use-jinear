package co.jinear.core.service.task.checklist;

import co.jinear.core.converter.task.ChecklistDtoConverter;
import co.jinear.core.model.dto.task.ChecklistDto;
import co.jinear.core.model.dto.task.ChecklistItemDto;
import co.jinear.core.model.entity.task.Checklist;
import co.jinear.core.model.vo.task.InitializeChecklistItemVo;
import co.jinear.core.model.vo.task.InitializeChecklistVo;
import co.jinear.core.repository.task.ChecklistRepository;
import co.jinear.core.service.passive.PassiveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistItemService checklistItemService;
    private final ChecklistDtoConverter checklistDtoConverter;
    private final ChecklistRetrieveService checklistRetrieveService;
    private final PassiveService passiveService;

    @Transactional
    public ChecklistDto initializeChecklist(InitializeChecklistVo initializeChecklistVo) {
        log.info("Initialize checklist has started. initializeChecklistVo: {}", initializeChecklistVo);
        Checklist checklist = mapInitialEntity(initializeChecklistVo);
        Checklist saved = checklistRepository.saveAndFlush(checklist);
        ChecklistItemDto initialItem = createInitialChecklistItem(initializeChecklistVo, saved);
        return convertAndSetInitialItem(checklist, initialItem);
    }

    public String passivizeChecklist(String checklistId) {
        log.info("Passivize checklist has started. checklistId: {]", checklistId);
        Checklist checklist = checklistRetrieveService.retrieveEntity(checklistId);
        String passiveId = passiveService.createUserActionPassive();
        checklist.setPassiveId(passiveId);
        checklistRepository.save(checklist);
        return passiveId;
    }

    public ChecklistDto updateChecklistTitle(String checklistId, String title) {
        log.info("Update checklist title has started. checklistId: {}, title: {}", checklistId, title);
        Checklist checklist = checklistRetrieveService.retrieveEntity(checklistId);
        checklist.setTitle(title);
        Checklist saved = checklistRepository.save(checklist);
        return checklistDtoConverter.convert(saved);
    }

    private ChecklistDto convertAndSetInitialItem(Checklist checklist, ChecklistItemDto initialItem) {
        ChecklistDto checklistDto = checklistDtoConverter.convert(checklist);
        Optional.ofNullable(initialItem).map(Set::of).ifPresent(checklistDto::setChecklistItems);
        return checklistDto;
    }

    private ChecklistItemDto createInitialChecklistItem(InitializeChecklistVo initializeChecklistVo, Checklist saved) {
        if (Objects.nonNull(initializeChecklistVo.getInitialItemLabel())) {
            InitializeChecklistItemVo initializeChecklistItemVo = mapInitialChecklistItemVo(initializeChecklistVo, saved);
            return checklistItemService.initialize(initializeChecklistItemVo);
        }
        return null;
    }

    private InitializeChecklistItemVo mapInitialChecklistItemVo(InitializeChecklistVo initializeChecklistVo, Checklist saved) {
        InitializeChecklistItemVo initializeChecklistItemVo = new InitializeChecklistItemVo();
        initializeChecklistItemVo.setChecklistId(saved.getChecklistId());
        initializeChecklistItemVo.setLabel(initializeChecklistVo.getInitialItemLabel());
        return initializeChecklistItemVo;
    }

    private Checklist mapInitialEntity(InitializeChecklistVo initializeChecklistVo) {
        Checklist checklist = new Checklist();
        checklist.setTaskId(initializeChecklistVo.getTaskId());
        checklist.setOwnerId(initializeChecklistVo.getOwnerId());
        checklist.setTitle(initializeChecklistVo.getTitle());
        return checklist;
    }
}
