package co.jinear.core.service.username;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.model.dto.username.UsernameDto;
import co.jinear.core.model.entity.username.Username;
import co.jinear.core.model.vo.username.InitializeUsernameVo;
import co.jinear.core.repository.ReservedUsernameRepository;
import co.jinear.core.repository.UsernameRepository;
import co.jinear.core.system.NormalizeHelper;
import co.jinear.core.system.RandomHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsernameService {

    private final UsernameRepository usernameRepository;
    private final ReservedUsernameRepository reservedUsernameRepository;
    private final ModelMapper modelMapper;

    public UsernameDto assignUsername(InitializeUsernameVo initializeUsernameVo) {
        log.info("Initialize username has started. initializeUsernameVo: {}", initializeUsernameVo);
        validateRelatedObjectHasNoUsername(initializeUsernameVo);
        validateUsernameIsNotReserved(initializeUsernameVo);
        String requestedHandle = NormalizeHelper.normalizeStrictly(initializeUsernameVo.getUsername());
        Optional<Username> existing = usernameRepository.findByUsername(requestedHandle);
        if (existing.isPresent()) {
            return handleUsernameExists(initializeUsernameVo);
        }
        Username username = saveUsername(initializeUsernameVo);
        return modelMapper.map(username, UsernameDto.class);
    }

    private UsernameDto handleUsernameExists(InitializeUsernameVo initializeUsernameVo) {
        log.info("Username already exist. Handling collision: {}", initializeUsernameVo);
        return Optional.of(initializeUsernameVo)
                .map(InitializeUsernameVo::getAppendRandomStrOnCollision)
                .filter(Boolean.TRUE::equals)
                .map(appendOnCollision -> appendNumAndAssign(initializeUsernameVo))
                .orElseThrow(BusinessException::new);
    }

    private UsernameDto appendNumAndAssign(InitializeUsernameVo initializeUsernameVo) {
        log.info("Appending random number and retrying.");
        appendRandomNumberToUsername(initializeUsernameVo);
        return assignUsername(initializeUsernameVo);
    }

    private InitializeUsernameVo appendRandomNumberToUsername(InitializeUsernameVo initializeUsernameVo) {
        StringBuilder sb = new StringBuilder();
        sb.append(initializeUsernameVo.getUsername());
        sb.append(RandomHelper.getRandomNumberInRange(0, 100));
        initializeUsernameVo.setUsername(sb.toString());
        return initializeUsernameVo;
    }

    private Username saveUsername(InitializeUsernameVo initializeUsernameVo) {
        String requestedHandle = NormalizeHelper.normalizeStrictly(initializeUsernameVo.getUsername());
        Username username = new Username();
        username.setRelatedObjectId(initializeUsernameVo.getRelatedObjectId());
        username.setRelatedObjectType(initializeUsernameVo.getRelatedObjectType());
        username.setUsername(requestedHandle);
        return usernameRepository.save(username);
    }

    private void validateRelatedObjectHasNoUsername(InitializeUsernameVo initializeUsernameVo) {
        Optional.of(initializeUsernameVo)
                .map(InitializeUsernameVo::getRelatedObjectId)
                .map(usernameRepository::countAllByRelatedObjectIdAndPassiveIdIsNull)
                .filter(count -> count > 0L)
                .ifPresent(count -> {
                    log.info("Account already has username");
                    throw new BusinessException();
                });
    }

    private void validateUsernameIsNotReserved(InitializeUsernameVo initializeUsernameVo) {
        Optional.of(initializeUsernameVo)
                .map(InitializeUsernameVo::getUsername)
                .map(reservedUsernameRepository::countAllByUsername)
                .filter(count -> count > 0L)
                .ifPresent(count -> {
                    log.info("Username is reserved");
                    throw new BusinessException();
                });
    }
}
