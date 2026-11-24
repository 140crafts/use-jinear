import type {StringKeys} from "@/locales/useTranslation";
import type {TaskInitializeReminderRequest} from "@/model/be/jinear-core";

type DraftReminder = TaskInitializeReminderRequest | null | undefined;
type DraftDate = Date | string | null | undefined;

export const countDraftReminders = (reminder: DraftReminder) =>
    [reminder?.beforeAssignedDate, reminder?.beforeDueDate, reminder?.specificRemindDate].filter(Boolean).length;

export const normalizeDraftReminder = (reminder: DraftReminder) =>
    countDraftReminders(reminder) == 0 ? null : reminder ?? null;

export const withoutAssignedDateReminder = (reminder: DraftReminder) =>
    normalizeDraftReminder(reminder && {...reminder, beforeAssignedDate: false});

export const withoutDueDateReminder = (reminder: DraftReminder) =>
    normalizeDraftReminder(reminder && {...reminder, beforeDueDate: false});

export const withoutRemindersOnMissingDates = (reminder: DraftReminder, assignedDate: DraftDate, dueDate: DraftDate) => {
    const withAssignedDate = assignedDate ? reminder : withoutAssignedDateReminder(reminder);
    return dueDate ? normalizeDraftReminder(withAssignedDate) : withoutDueDateReminder(withAssignedDate);
};

export const reminderCountLabel = (t: (key: StringKeys) => string, count: number) =>
    count == 1 ? t("taskReminderCountOne") : t("taskReminderCountMany").replace("${count}", `${count}`);
