package co.jinear.core.system.gcloud.googleapis.model.calendar.enumtype;

import java.util.List;

@SuppressWarnings("java:S115")
public enum GoogleCalendarAccessRoleType {
    freeBusyReader,
    reader,
    writer,
    owner;

    public Boolean isReadOnly() {
        return !List.of(writer, owner).contains(this);
    }
}
