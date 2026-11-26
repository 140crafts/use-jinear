import useTranslation from "@/locales/useTranslation";
import type {TaskInitializeReminderRequest} from "@/model/be/jinear-core";
import {countDraftReminders, reminderCountLabel} from "@/util/taskReminderDraftUtils";
import {format} from "date-fns";
import React from "react";
import {firstRemindDate, type ReminderDate} from "../reminderForm";
import styles from "./ReminderSummary.module.css";

interface ReminderSummaryProps {
    request: TaskInitializeReminderRequest;
    assignedDate: ReminderDate;
    dueDate: ReminderDate;
}

const ReminderSummary: React.FC<ReminderSummaryProps> = ({request, assignedDate, dueDate}) => {
    const {t} = useTranslation();
    const count = countDraftReminders(request);
    const firstDate = firstRemindDate(request, assignedDate, dueDate);
    const repeatType = request.specificRemindDateRepeatType;
    const repeatEnd = request.specificRemindRepeatEnd;

    if (count == 0) {
        return <div className={styles.container}>{t("taskNewReminderSummaryNone")}</div>;
    }

    const repeatLabel = repeatType && repeatType != "NONE" ? t(`taskNewReminderModalReminderRepeatType_${repeatType}`) : null;
    const repeatLine = repeatLabel && repeatEnd
        ? t("taskNewReminderSummaryRepeatUntil").replace("${repeat}", repeatLabel).replace("${date}", format(repeatEnd, t("dateFormat")))
        : repeatLabel;

    return (
        <div className={styles.container}>
            <b>{reminderCountLabel(t, count)}</b>
            {firstDate && <span className={styles.detail}>{t("taskNewReminderSummaryFirst").replace("${date}", format(firstDate, t("dateTimeFormat")))}</span>}
            {repeatLine && <span className={styles.detail}>{repeatLine}</span>}
        </div>
    );
};

export default ReminderSummary;
