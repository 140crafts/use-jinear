package co.jinear.core.system;

import de.huxhorn.sulky.ulid.ULID;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

@UtilityClass
public class RandomHelper {
    private static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 16;
    private static final Random random = new SecureRandom();

    public static String generateULID(){
        return new ULID().nextULID().toLowerCase(Locale.ROOT);
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

    public static String generateRandomPassword() {
        return random.ints(PASSWORD_LENGTH, 0, PASSWORD_CHARACTERS.length())
                .mapToObj(PASSWORD_CHARACTERS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
