import DateTimeInput from "@/components/dateTimeInput/DateTimeInput";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import type {RepeatType} from "@/model/be/jinear-core";
import {startOfToday} from "date-fns";
import React, {type ChangeEvent} from "react";
import {isRepeating, MINUTE_RESOLUTION, REPEAT_TYPES, type ReminderForm} from "../reminderForm";
import styles from "./ReminderRepeatSection.module.css";

interface ReminderRepeatSectionProps {
    form: ReminderForm;
    onChange: (patch: Partial<ReminderForm>) => void;
    error?: StringKeys;
}

const ENDS_NEVER = "NEVER";
const ENDS_ON_DATE = "ON_DATE";

const ReminderRepeatSection: React.FC<ReminderRepeatSectionProps> = ({form, onChange, error}) => {
    const {t} = useTranslation();

    const endsSegments = [
        {label: t("taskNewReminderRepeatEndsNever"), value: ENDS_NEVER},
        {label: t("taskNewReminderRepeatEndsOnDate"), value: ENDS_ON_DATE}
    ];

    const onRepeatTypeChange = (event: ChangeEvent<HTMLSelectElement>) => {
        onChange({repeatType: event.target.value as RepeatType});
    };

    const onEndsChange = (value: string) => {
        onChange({endsOnDate: value == ENDS_ON_DATE});
    };

    const onRepeatEndChange = (date?: Date | null) => {
        onChange({repeatEnd: date ?? null});
    };

    return (
        <div className={styles.container}>
            <label className={styles.field} htmlFor="reminder-repeat-type">
                <span className={styles.fieldLabel}>{t("taskNewReminderModalRepeat")}</span>
                <select id="reminder-repeat-type" value={form.repeatType} onChange={onRepeatTypeChange}>
                    {REPEAT_TYPES.map((type: RepeatType) => (
                        <option key={`repeat-type-${type}`} value={type}>
                            {t(`taskNewReminderModalReminderRepeatType_${type}`)}
                        </option>
                    ))}
                </select>
            </label>

            {isRepeating(form) && (
                <div className={styles.field}>
                    <span className={styles.fieldLabel}>{t("taskNewReminderRepeatEndsLabel")}</span>
                    <SegmentedControl
                        id="reminder-repeat-ends"
                        name="reminder-repeat-ends"
                        segments={endsSegments}
                        defaultIndex={form.endsOnDate ? 1 : 0}
                        callback={onEndsChange}
                        segmentLabelClassName={styles.endsSegmentLabel}
                    />
                </div>
            )}

            {isRepeating(form) && form.endsOnDate && (
                <div className={styles.field}>
                    <span/>
                    <DateTimeInput
                        id="reminder-repeat-end"
                        type="date-time"
                        value={form.repeatEnd}
                        setValue={onRepeatEndChange}
                        valuePrecise={true}
                        allowEmptyDate={false}
                        keepTimeOnDatePick={true}
                        disabledBefore={startOfToday()}
                        minuteResolution={MINUTE_RESOLUTION}
                        dateButtonLabel={t("datePickerSelectDate")}
                        datePickerModalTitle={t("taskNewReminderModalRepeatEndDateLabel")}
                    />
                </div>
            )}

            {error && <div className={styles.error}>{t(error)}</div>}
        </div>
    );
};

export default ReminderRepeatSection;
