package co.jinear.core.model.enumtype.management;

import co.jinear.core.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstanceFlagType {

    REGISTER_WITH_MAIL(0, Boolean.class),
    FORGOT_PASSWORD(1, Boolean.class),
    SIGN_IN_WITH_APPLE(2, Boolean.class),
    SIGN_IN_WITH_GOOGLE(3, Boolean.class),
    SIGN_IN_WITH_EMAIL_CODE(4, Boolean.class),
    WORKSPACE_INIT(5, Boolean.class),
    ATTACH_GOOGLE_CALENDAR(6, Boolean.class);

    private final int value;
    private final Class<?> valueType;

    public Object parse(String flagValue) {
        if (Boolean.class.equals(valueType)) {
            return Boolean.valueOf(flagValue);
        }
        return flagValue;
    }

    public String format(Object value) {
        if (!valueType.isInstance(value)) {
            throw new BusinessException("common.error.system.validation-fail");
        }
        return value.toString();
    }

}
