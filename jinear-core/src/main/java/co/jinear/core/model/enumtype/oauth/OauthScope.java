package co.jinear.core.model.enumtype.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The resource scopes this authorization server can grant.
 * <p>
 * Scopes are per resource and split read from write, so a user consenting to task writes
 * does not also hand over their calendar. They describe access to Jinear data, not to any
 * one transport, and a consent screen for a second consumer would list the same values.
 * <p>
 * There is deliberately no projects scope. Projects are a deprecated feature, so nothing
 * exposes them. A token minted before the removal may still carry projects:read or
 * projects:write, which is harmless because nothing requires them and
 * {@link co.jinear.core.service.oauth.provider.OauthScopeService#parse} drops any value
 * this enum does not define.
 * <p>
 * Two entries below are read only for reasons that come from the MCP tool catalog, which
 * is the only consumer today. Those notes stay with the values so the asymmetry is not
 * mistaken for an oversight.
 */
@Getter
@AllArgsConstructor
public enum OauthScope {

    WORKSPACE_READ("workspace:read"),
    TASKS_READ("tasks:read"),
    TASKS_WRITE("tasks:write"),
    /**
     * Read only. Jinear has no calendar events of its own: the calendar is an aggregate
     * of tasks with dates and of events synced from an attached Google Calendar. Writing
     * an event would mean writing to Google on the user's behalf, which is a third party
     * API nothing here proxies. Scheduling work is a task with a date.
     */
    CALENDAR_READ("calendar:read"),
    /**
     * Read only. A note's body is a CRDT document authored by the editor, and seeding one
     * from plain text would produce a note whose title and body disagree the moment
     * somebody opens it.
     */
    NOTES_READ("notes:read"),
    FILES_READ("files:read"),
    /** Not a resource scope. Its presence is what makes us mint a refresh token. */
    OFFLINE_ACCESS("offline_access");

    private final String value;

    public static Optional<OauthScope> ofValue(String value) {
        return Arrays.stream(values())
                .filter(scope -> scope.value.equals(value))
                .findFirst();
    }

    /**
     * Every scope a client may ask for, in a stable order for the discovery documents.
     * <p>
     * One flat set because MCP is the only resource. A second resource makes the answer
     * depend on which resource the client asked about.
     */
    public static Set<String> allValues() {
        return Arrays.stream(values())
                .map(OauthScope::getValue)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }
}
