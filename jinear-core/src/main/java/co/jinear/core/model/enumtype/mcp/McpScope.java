package co.jinear.core.model.enumtype.mcp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * OAuth scopes this resource server understands.
 * <p>
 * Scopes are per resource and split read from write, so a user consenting to task
 * writes does not also hand over their calendar. Delete tools reuse the write scope
 * of their resource and are marked destructive in the tool annotation instead, which
 * is what drives the per-call confirmation prompt in the client.
 */
@Getter
@AllArgsConstructor
public enum McpScope {

    WORKSPACE_READ("workspace:read"),
    TASKS_READ("tasks:read"),
    TASKS_WRITE("tasks:write"),
    PROJECTS_READ("projects:read"),
    PROJECTS_WRITE("projects:write"),
    /**
     * Read only. Jinear has no calendar events of its own: the calendar is an aggregate
     * of tasks with dates and of events synced from an attached Google Calendar. Writing
     * an event would mean writing to Google on the user's behalf, which is a third party
     * API this connector deliberately does not proxy. Scheduling work is create_task.
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

    public static Optional<McpScope> ofValue(String value) {
        return Arrays.stream(values())
                .filter(scope -> scope.value.equals(value))
                .findFirst();
    }

    /** Every scope a client may ask for, in a stable order for the discovery documents. */
    public static Set<String> allValues() {
        return Arrays.stream(values())
                .map(McpScope::getValue)
                .collect(LinkedHashSet::new, Set::add, Set::addAll);
    }
}
