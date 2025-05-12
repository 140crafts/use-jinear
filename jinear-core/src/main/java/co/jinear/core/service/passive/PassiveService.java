package co.jinear.core.service.passive;

import co.jinear.core.exception.NotFoundException;
import co.jinear.core.model.entity.passive.Passive;
import co.jinear.core.model.enumtype.passive.PassiveReason;
import co.jinear.core.model.vo.passive.CreatePassiveVo;
import co.jinear.core.repository.PassiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PassiveService {

    private final PassiveRepository passiveRepository;

    public String createPassive(CreatePassiveVo createPassiveVo) {
        Passive passive = mapPassive(createPassiveVo);
        Passive saved = passiveRepository.save(passive);
        return saved.getPassiveId();
    }

    public String createUserActionPassive() {
        return createPassive(CreatePassiveVo.builder()
                .reason(PassiveReason.USER_ACTION.name())
                .reasonType(PassiveReason.USER_ACTION)
                .build());
    }

    public String createUserActionPassive(String accountId) {
        return createPassive(CreatePassiveVo.builder()
                .responsibleAccountId(accountId)
                .reason(PassiveReason.USER_ACTION.name())
                .reasonType(PassiveReason.USER_ACTION)
                .build());
    }

    public String createSystemActionPassive(String accountId) {
        return createPassive(CreatePassiveVo.builder()
                .responsibleAccountId(accountId)
                .reason(PassiveReason.SYSTEM.name())
                .reasonType(PassiveReason.SYSTEM)
                .build());
    }

    public String createSystemActionPassive() {
        return createPassive(CreatePassiveVo.builder()
                .reason(PassiveReason.SYSTEM.name())
                .reasonType(PassiveReason.SYSTEM)
                .build());
    }

    public void assignOwnership(String passiveId, String accountId) {
        log.info("Assign ownership to passive has started. passiveId: {}, accountId: {}", passiveId, accountId);
        Passive passive = passiveRepository.findByPassiveId(passiveId).orElseThrow(NotFoundException::new);
        passive.setResponsibleAccountId(accountId);
        passiveRepository.save(passive);
    }

    private Passive mapPassive(CreatePassiveVo createPassiveVo) {
        Passive passive = new Passive();
        passive.setResponsibleAccountId(createPassiveVo.getResponsibleAccountId());
        passive.setRelatedObjectId(createPassiveVo.getRelatedObjectId());
        passive.setReason(createPassiveVo.getReason());
        passive.setReasonType(createPassiveVo.getReasonType());
        passive.setReasonSubtype(createPassiveVo.getReasonSubtype());
        return passive;
    }
}