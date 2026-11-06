package co.jinear.core.validator;

import co.jinear.core.exception.BusinessException;
import co.jinear.core.validator.common.ColorValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ColorValidatorTest {

    private final ColorValidator colorValidator = new ColorValidator();

    @ParameterizedTest
    @ValueSource(strings = {"FF6600", "ff6600", "0000fF", "123456"})
    void sixHexDigitsPass(String color) {
        assertThatCode(() -> colorValidator.validateHexColorIfPresent(color)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void anAbsentColorPasses(String color) {
        assertThatCode(() -> colorValidator.validateHexColorIfPresent(color)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ZZZZZZ", "GGGGGG", "#FF6600", "FFF", "FF66001", " ", "FF 600"})
    void anythingElseIsRefused(String color) {
        assertThatThrownBy(() -> colorValidator.validateHexColorIfPresent(color))
                .isInstanceOf(BusinessException.class)
                .hasMessage("common.error.invalid-color");
    }
}
