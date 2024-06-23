package co.jinear.core.service.robot;

import co.jinear.core.exception.NoAccessException;
import co.jinear.core.system.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotTokenValidator {

    private final RobotRetrieveService robotRetrieveService;
    private final JwtHelper jwtHelper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void validate(String token) {
        String robotId = jwtHelper.getSubjectFromToken(token);
        String hashedToken = robotRetrieveService.retrieveHashedToken(robotId);
        log.info("Validate token has started for robot. robotId: {}", robotId);
        if (!bCryptPasswordEncoder.matches(token, hashedToken)) {
            throw new NoAccessException();
        }
    }
}
