package co.jinear.core.system;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHelperTest {

    private static final int SAMPLE_SIZE = 1000;

    @Test
    void randomPasswordHasSixteenLettersAndDigits() {
        String password = RandomHelper.generateRandomPassword();

        assertTrue(password.matches("[A-Za-z0-9]{16}"));
    }

    @Test
    void randomPasswordsDoNotRepeat() {
        Set<String> passwords = Stream.generate(RandomHelper::generateRandomPassword)
                .limit(SAMPLE_SIZE)
                .collect(Collectors.toSet());

        assertEquals(SAMPLE_SIZE, passwords.size());
    }
}
