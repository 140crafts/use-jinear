import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import type {TaskInitializeReminderRequest, TaskInitializeRequest} from "@/model/be/jinear-core";
import {useAppDispatch} from "@/store";
import {popNewReminderModal} from "@/store/slice/modalSlice";
import useTranslation from "@/locales/useTranslation";
import {
    countDraftReminders,
    reminderCountLabel,
    withoutAssignedDateReminder,
    withoutDueDateReminder
} from "@/util/taskReminderDraftUtils";
import React, {useEffect, useState} from "react";
import type {UseFormSetValue} from "react-hook-form";
import toast from "react-hot-toast";
import {LuBell} from "react-icons/lu";
import styles from "./ReminderPickerButton.module.css";

interface ReminderPickerButtonProps {
    assignedDate?: Date | string | null;
    hasPreciseAssignedDate?: boolean;
    dueDate?: Date | string | null;
    hasPreciseDueDate?: boolean;
    setValue: UseFormSetValue<TaskInitializeRequest>;
}

const toDate = (value?: Date | string | null) => value ? new Date(value) : null;

const ReminderPickerButton: React.FC<ReminderPickerButtonProps> = ({
                                                                       assignedDate,
                                                                       hasPreciseAssignedDate,
                                                                       dueDate,
                                                                       hasPreciseDueDate,
                                                                       setValue
                                                                   }) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [reminder, setReminder] = useState<TaskInitializeReminderRequest | null>(null);
    const count = countDraftReminders(reminder);

    useEffect(() => {
        setValue("reminder", reminder ?? undefined);
    }, [reminder, setValue]);

    useEffect(() => {
        if (!assignedDate && reminder?.beforeAssignedDate) {
            setReminder(withoutAssignedDateReminder(reminder));
            toast(t("newTaskFormReminderAssignedDateRemoved"));
        }
    }, [assignedDate, reminder]);

    useEffect(() => {
        if (!dueDate && reminder?.beforeDueDate) {
            setReminder(withoutDueDateReminder(reminder));
            toast(t("newTaskFormReminderDueDateRemoved"));
        }
    }, [dueDate, reminder]);

    const popReminderModal = () => {
        dispatch(popNewReminderModal({
            visible: true,
            draft: {
                assignedDate: toDate(assignedDate),
                hasPreciseAssignedDate,
                dueDate: toDate(dueDate),
                hasPreciseDueDate,
                reminder
            },
            onDraftSave: setReminder
        }));
    };

    const clear = () => {
        setReminder(null);
    };

    return (
        <div className={styles.container}>
            <SelectDeselectButton
                hasSelection={count != 0}
                onPickClick={popReminderModal}
                selectedComponent={
                    <div className={styles.selectedContainer}>
                        <LuBell/>
                        {reminderCountLabel(t, count)}
                    </div>
                }
                emptySelectionLabel={t("newTaskFormPickReminderButtonLabel")}
                onUnpickClick={clear}
            />
        </div>
    );
};

export default ReminderPickerButton;
