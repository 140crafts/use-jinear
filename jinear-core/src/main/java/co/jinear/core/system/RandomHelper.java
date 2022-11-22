package co.jinear.core.system;

import co.jinear.core.exception.BusinessException;
import com.google.common.io.BaseEncoding;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Slf4j
@UtilityClass
public class RandomHelper {
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String DASH = "-";
    private static final Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.error("Cannot initialize secure random instance.", e);
            throw new BusinessException();
        }
    }

    public static String generateEmailCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRandomNumberInRange(100000, 999999));
        return sb.toString();
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static String generateUniqueSentenceLink(String sentence) {
        String id = generateSentenceId();
        return Optional.of(sentence)
                .map(String::trim)
                .map(s -> s.replaceAll(SPACE, DASH))
                .map(s -> s.substring(0, Math.min(s.length(), 35)))
                .map(NormalizeHelper::removeAccent)
                .map(NormalizeHelper::removeNonAscii)
                .map(s -> s + DASH)
                .map(s -> s + id)
                .orElseThrow(BusinessException::new);
    }

    public static String generateRandomString() {
        return generateUniqueSentenceLink(EMPTY);
    }

    private static String generateSentenceId() {
        String id;
        try {
            id = BaseEncoding.base64().encode(RandomHelper.generateEmailCode().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Error generating base64 unique sentence id", e);
            id = RandomHelper.generateEmailCode();
        }
        return id;
    }
}
