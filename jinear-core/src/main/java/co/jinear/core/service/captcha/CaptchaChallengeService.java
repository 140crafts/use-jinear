package co.jinear.core.service.captcha;

import co.jinear.core.exception.NotValidException;
import co.jinear.core.model.dto.captcha.CaptchaChallengeDto;
import co.jinear.core.model.vo.captcha.CaptchaResolveVo;
import co.jinear.core.repository.captcha.CaptchaRepository;
import co.jinear.ratelimiter.resolver.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaChallengeService {

    private final CaptchaRepository captchaRepository;
    private final ClientIpResolver clientIpResolver;

    private static final int PREFIX_COUNT = 2;
    private static final NavigableMap<Integer, Integer> DIFFICULTY_MAP;

    static {
        DIFFICULTY_MAP = new TreeMap<>();
        DIFFICULTY_MAP.put(0, 4);
        DIFFICULTY_MAP.put(5, 5);
        DIFFICULTY_MAP.put(15, 6);
        DIFFICULTY_MAP.put(30, 50);
    }

    public CaptchaChallengeDto initialize(HttpServletRequest httpServletRequest) {
        String key = clientIpResolver.resolveClientIp(httpServletRequest);
        return initialize(key);
    }

    public CaptchaChallengeDto initialize(String key) {
        log.info("Initialize captcha challenge has started for key: {}", key);
        int requestsInWindow = captchaRepository.incrementAndGetRequestCount(key);
        int difficulty = DIFFICULTY_MAP.floorEntry(requestsInWindow).getValue();
        CaptchaChallengeDto captchaChallengeDto = mapCaptchaChallengeDto(difficulty);
        captchaRepository.storeChallenge(key, captchaChallengeDto);
        return captchaChallengeDto;
    }

    public boolean verifySolution(HttpServletRequest httpServletRequest, List<CaptchaResolveVo> resolveVos) {
        if (Objects.isNull(resolveVos) || resolveVos.isEmpty()) {
            throw new NotValidException();
        }
        String key = clientIpResolver.resolveClientIp(httpServletRequest);
        return verifySolution(key, resolveVos);
    }

    public boolean verifySolution(String key, List<CaptchaResolveVo> resolveVos) {
        String[] prefixes = resolveVos.stream().map(CaptchaResolveVo::getPrefix).toArray(String[]::new);
        String prefixesJoin = StringUtils.join(prefixes);
        log.info("Verify solution has started. key: {}, prefixesJoin: {}", key, prefixesJoin);
        CaptchaChallengeDto captchaChallengeDto = captchaRepository.getChallenge(key, prefixesJoin);
        int difficulty = captchaChallengeDto.getDifficulty();
        return resolveVos.stream().allMatch(resolveVo -> verify(resolveVo.getPrefix(), resolveVo.getNonce(), difficulty));
    }

    private boolean verify(String prefix, long nonce, int difficulty) {
        String input = prefix + nonce;
        String hash = sha256(input);
        String target = new String(new char[difficulty]).replace('\0', '0');
        return hash.startsWith(target);
    }

    private CaptchaChallengeDto mapCaptchaChallengeDto(int difficulty) {
        String[] strings = generatePrefixes(PREFIX_COUNT);
        CaptchaChallengeDto captchaChallengeDto = new CaptchaChallengeDto();
        captchaChallengeDto.setPrefixes(strings);
        captchaChallengeDto.setDifficulty(difficulty);
        return captchaChallengeDto;
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String[] generatePrefixes(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                .toArray(String[]::new);
    }
}
