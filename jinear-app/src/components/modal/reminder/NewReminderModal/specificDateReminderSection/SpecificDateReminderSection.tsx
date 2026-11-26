import DateTimeInput from "@/components/dateTimeInput/DateTimeInput";
import useTranslation from "@/locales/useTranslation";
import {startOfToday} from "date-fns";
import React from "react";
import {MINUTE_RESOLUTION, type ReminderForm, type ReminderFormErrors} from "../reminderForm";
import ReminderPresetChips from "../reminderPresetChips/ReminderPresetChips";
import ReminderRepeatSection from "../reminderRepeatSection/ReminderRepeatSection";
import styles from "./SpecificDateReminderSection.module.css";

interface SpecificDateReminderSectionProps {
    form: ReminderForm;
    onChange: (patch: Partial<ReminderForm>) => void;
    errors: ReminderFormErrors;
}

const SpecificDateReminderSection: React.FC<SpecificDateReminderSectionProps> = ({form, onChange, errors}) => {
    const {t} = useTranslation();

    const onRemindDateChange = (date?: Date | null) => {
        onChange({remindDate: date ?? null});
    };

    return (
        <div className={styles.container}>
            <div className={styles.dateContainer}>
                <div className={styles.dateOptions}>
                    <ReminderPresetChips value={form.remindDate} onSelect={onRemindDateChange}/>
                    <DateTimeInput
                        id="reminder-specific-date"
                        type="date-time"
                        value={form.remindDate}
                        setValue={onRemindDateChange}
                        valuePrecise={true}
                        allowEmptyDate={false}
                        keepTimeOnDatePick={true}
                        disabledBefore={startOfToday()}
                        minuteResolution={MINUTE_RESOLUTION}
                        dateButtonLabel={t("datePickerSelectDate")}
                        datePickerModalTitle={t("taskNewReminderModalSpecificDateLabel")}
                    />
                </div>
                {errors.remindDate && <div className={styles.error}>{t(errors.remindDate)}</div>}
            </div>
            <ReminderRepeatSection form={form} onChange={onChange} error={errors.repeatEnd}/>
        </div>
    );
};

export default SpecificDateReminderSection;
