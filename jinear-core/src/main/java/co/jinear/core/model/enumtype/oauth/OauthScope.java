package co.jinear.core.model.enumtype.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum OauthScope {

    WORKSPACE_READ("workspace:read"),
    TASKS_READ("tasks:read"),
    TASKS_WRITE("tasks:write"),
    CALENDAR_READ("calendar:read"),
    NOTES_READ("notes:read"),
    FILES_READ("files:read"),
    OFFLINE_ACCESS("offline_access");

    private final String value;

    public static Optional<OauthScope> ofValue(String value) {
        return Arrays.stream(values())
                .filter(scope -> scope.value.equals(value))
                .findFirst();
    }

    public static Set<String> allValues() {
        return Arrays.stream(values())
                .map(OauthScope::getValue)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }
}
