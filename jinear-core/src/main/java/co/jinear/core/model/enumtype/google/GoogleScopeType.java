package co.jinear.core.model.enumtype.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Getter
@AllArgsConstructor
public enum GoogleScopeType {

    //BASIC
    OPEN_ID(0, List.of("openid")),
    USERINFO_PROFILE(1, List.of("profile", "https://www.googleapis.com/auth/userinfo.profile")),
    USERINFO_EMAIL(2, List.of("email", "https://www.googleapis.com/auth/userinfo.email")),

    //CALENDAR
    CALENDAR(10, List.of("https://www.googleapis.com/auth/calendar")),
    CALENDAR_EVENTS(11, List.of("https://www.googleapis.com/auth/calendar.events")),
    CALENDAR_SETTINGS_READONLY(12, List.of("https://www.googleapis.com/auth/calendar.settings.readonly")),
    ADMIN_DIRECTORY_RESOURCE_CALENDAR_READONLY(13, List.of("https://www.googleapis.com/auth/admin.directory.resource.calendar.readonly")),
    CONTACTS(14, List.of("https://www.googleapis.com/auth/contacts")),
    CONTACTS_OTHER_READONLY(15, List.of("https://www.googleapis.com/auth/contacts.other.readonly")),
    DIRECTORY_READONLY(16, List.of("https://www.googleapis.com/auth/directory.readonly")),

    //MAIL
    MAIL(20, List.of("https://mail.google.com/")),
    GMAIL_ADDONS_CURRENT_MESSAGE_ACTION(21, List.of("https://www.googleapis.com/auth/gmail.addons.current.message.action")),
    GMAIL_ADDONS_CURRENT_MESSAGE_METADATA(22, List.of("https://www.googleapis.com/auth/gmail.addons.current.message.metadata")),
    GMAIL_ADDONS_CURRENT_MESSAGE_READONLY(23, List.of("https://www.googleapis.com/auth/gmail.addons.current.message.readonly")),
    GMAIL_METADATA(24, List.of("https://www.googleapis.com/auth/gmail.metadata")),
    GMAIL_MODIFY(25, List.of("https://www.googleapis.com/auth/gmail.modify")),
    GMAIL_READONLY(26, List.of("https://www.googleapis.com/auth/gmail.readonly"));

    private final int value;
    private final List<String> keys;

    public static List<String> getBasicScopes() {
        return Stream.of(OPEN_ID, USERINFO_PROFILE, USERINFO_EMAIL)
                .map(GoogleScopeType::getKeys)
                .map(List::iterator)
                .map(Iterator::next)
                .toList();
    }

    public static List<String> getCalendarScopes() {
        return Stream.of(
                        CALENDAR,
                        CALENDAR_EVENTS,
                        CALENDAR_SETTINGS_READONLY,
                        ADMIN_DIRECTORY_RESOURCE_CALENDAR_READONLY,
                        CONTACTS,
                        CONTACTS_OTHER_READONLY,
                        DIRECTORY_READONLY
                )
                .map(GoogleScopeType::getKeys)
                .map(List::iterator)
                .map(Iterator::next)
                .toList();
    }

    public static List<String> getMailScopes() {
        return Stream.of(
                        MAIL,
                        GMAIL_ADDONS_CURRENT_MESSAGE_ACTION,
                        GMAIL_ADDONS_CURRENT_MESSAGE_METADATA,
                        GMAIL_ADDONS_CURRENT_MESSAGE_READONLY,
                        GMAIL_METADATA,
                        GMAIL_MODIFY,
                        GMAIL_READONLY
                )
                .map(GoogleScopeType::getKeys)
                .map(List::iterator)
                .map(Iterator::next)
                .toList();
    }

    public static GoogleScopeType fromString(String scope) {
        return Arrays.stream(GoogleScopeType.values())
                .filter(type -> type.getKeys().contains(scope))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Scope not found: {}", scope);
                    return null;
                });
    }
}
