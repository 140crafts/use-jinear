import type {StringKeys} from "@/locales/useTranslation";
import type {RepeatType, TaskInitializeReminderRequest} from "@/model/be/jinear-core";
import {
    addDays,
    addHours,
    addMinutes,
    getMinutes,
    isAfter,
    isFuture,
    isPast,
    min,
    nextMonday,
    setHours,
    setMinutes,
    startOfDay,
    startOfHour
} from "date-fns";

export type ReminderDate = Date | string | null | undefined;

export type ReminderOptionState = "AVAILABLE" | "MISSING" | "PASSED" | "EXISTS";

export interface ReminderForm {
    atAssignedDate: boolean;
    atDueDate: boolean;
    atSpecificDate: boolean;
    remindDate: Date | null;
    repeatType: RepeatType;
    endsOnDate: boolean;
    repeatEnd: Date | null;
}

export interface ReminderFormErrors {
    remindDate?: StringKeys;
    repeatEnd?: StringKeys;
}

export interface ReminderOptionStates {
    assignedDate: ReminderOptionState;
    dueDate: ReminderOptionState;
}

export interface ReminderPreset {
    labelKey: StringKeys;
    date: Date;
}

export const MINUTE_RESOLUTION = 15;

export const REPEAT_TYPES: RepeatType[] = [
    "NONE",
    "HOURLY",
    "DAILY",
    "WEEKLY",
    "BIWEEKLY",
    "MONTHLY",
    "EVERY_3_MONTHS",
    "EVERY_6_MONTHS",
    "YEARLY"
];

const atTime = (day: Date, hours: number, minutes = 0) => setMinutes(setHours(startOfDay(day), hours), minutes);

const roundUpToResolution = (date: Date) => {
    const roundedMinutes = Math.ceil(getMinutes(date) / MINUTE_RESOLUTION) * MINUTE_RESOLUTION;
    return addMinutes(startOfHour(date), roundedMinutes);
};

const toDate = (date: ReminderDate) => date ? new Date(date) : null;

const tomorrowMorning = () => atTime(addDays(new Date(), 1), 9);

export const reminderPresets = (now: Date): ReminderPreset[] => ([
    {labelKey: "taskNewReminderPresetInOneHour", date: roundUpToResolution(addHours(now, 1))},
    {labelKey: "taskNewReminderPresetThisEvening", date: atTime(now, 18)},
    {labelKey: "taskNewReminderPresetTomorrowMorning", date: tomorrowMorning()},
    {labelKey: "taskNewReminderPresetNextMonday", date: atTime(nextMonday(now), 9)}
] as ReminderPreset[]).filter(preset => isAfter(preset.date, now));

const DEFAULT_FORM: Omit<ReminderForm, "remindDate"> = {
    atAssignedDate: false,
    atDueDate: false,
    atSpecificDate: true,
    repeatType: "NONE",
    endsOnDate: false,
    repeatEnd: null
};

export const initialReminderForm = (reminder?: TaskInitializeReminderRequest | null): ReminderForm => {
    if (!reminder) {
        return {...DEFAULT_FORM, remindDate: tomorrowMorning()};
    }
    const remindDate = toDate(reminder.specificRemindDate);
    const repeatEnd = toDate(reminder.specificRemindRepeatEnd);
    return {
        atAssignedDate: reminder.beforeAssignedDate ?? false,
        atDueDate: reminder.beforeDueDate ?? false,
        atSpecificDate: remindDate != null,
        remindDate: remindDate ?? tomorrowMorning(),
        repeatType: reminder.specificRemindDateRepeatType ?? "NONE",
        endsOnDate: repeatEnd != null,
        repeatEnd
    };
};

export const reminderOptionState = (date: ReminderDate, hasExistingReminder: boolean): ReminderOptionState => {
    if (hasExistingReminder) {
        return "EXISTS";
    }
    if (!date) {
        return "MISSING";
    }
    return isPast(new Date(date)) ? "PASSED" : "AVAILABLE";
};

const OPTION_STATE_HINTS: Record<Exclude<ReminderOptionState, "AVAILABLE" | "MISSING">, StringKeys> = {
    PASSED: "taskNewReminderHintDatePassed",
    EXISTS: "taskNewReminderHintAlreadyExists"
};

export const reminderOptionHint = (state: ReminderOptionState, missingHint: StringKeys): StringKeys | null => {
    if (state == "AVAILABLE") {
        return null;
    }
    return state == "MISSING" ? missingHint : OPTION_STATE_HINTS[state];
};

export const isRepeating = (form: ReminderForm) => form.repeatType != "NONE";

const endsOnDate = (form: ReminderForm) => form.atSpecificDate && isRepeating(form) && form.endsOnDate;

const validateRemindDate = (form: ReminderForm): StringKeys | undefined => {
    if (!form.atSpecificDate) {
        return undefined;
    }
    if (!form.remindDate) {
        return "taskNewReminderProvideSpecificDate";
    }
    return isFuture(form.remindDate) ? undefined : "taskNewReminderProvideFutureDate";
};

const validateRepeatEnd = (form: ReminderForm): StringKeys | undefined => {
    if (!endsOnDate(form)) {
        return undefined;
    }
    if (!form.repeatEnd) {
        return "taskNewReminderProvideSpecificDate";
    }
    const endsAfterRemindDate = form.remindDate == null || isAfter(form.repeatEnd, form.remindDate);
    return endsAfterRemindDate ? undefined : "taskNewReminderRepeatEndAfterRemindDate";
};

export const validateReminderForm = (form: ReminderForm): ReminderFormErrors => ({
    remindDate: validateRemindDate(form),
    repeatEnd: validateRepeatEnd(form)
});

export const hasReminderFormErrors = (errors: ReminderFormErrors) => Object.values(errors).some(Boolean);

export const toReminderRequest = (form: ReminderForm, optionStates: ReminderOptionStates): TaskInitializeReminderRequest => ({
    beforeAssignedDate: optionStates.assignedDate == "AVAILABLE" && form.atAssignedDate,
    beforeDueDate: optionStates.dueDate == "AVAILABLE" && form.atDueDate,
    specificRemindDate: form.atSpecificDate ? form.remindDate : null,
    specificRemindDateRepeatType: form.atSpecificDate ? form.repeatType : null,
    specificRemindRepeatEnd: endsOnDate(form) ? form.repeatEnd : null
});

export const firstRemindDate = (request: TaskInitializeReminderRequest, assignedDate: ReminderDate, dueDate: ReminderDate) => {
    const dates = [
        request.beforeAssignedDate ? toDate(assignedDate) : null,
        request.beforeDueDate ? toDate(dueDate) : null,
        request.specificRemindDate ?? null
    ].filter((date): date is Date => date != null);
    return dates.length == 0 ? null : min(dates);
};
