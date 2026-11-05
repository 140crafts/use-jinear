package co.jinear.core.validator.common;

import co.jinear.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ColorValidator {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^[0-9A-Fa-f]{6}$");

    public void validateHexColorIfPresent(String color) {
        boolean absent = StringUtils.isEmpty(color);
        if (!absent && !HEX_COLOR_PATTERN.matcher(color).matches()) {
            throw new BusinessException("common.error.invalid-color");
        }
    }
}
