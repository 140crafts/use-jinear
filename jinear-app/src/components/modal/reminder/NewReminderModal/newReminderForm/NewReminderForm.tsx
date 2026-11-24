import Button, {ButtonVariants} from "@/components/button";
import GradientLine from "@/components/gradientLine/GradientLine";
import useTranslation from "@/locales/useTranslation";
import type {NewReminderDraft} from "@/model/app/store/modal/modalState";
import type {TaskDto, TaskInitializeReminderRequest, TaskReminderType} from "@/model/be/jinear-core";
import {useInitializeTaskReminderMutation} from "@/store/api/taskReminderApi";
import {countDraftReminders, normalizeDraftReminder} from "@/util/taskReminderDraftUtils";
import {format} from "date-fns";
import React, {type FormEvent, useEffect, useState} from "react";
import {
    hasReminderFormErrors,
    initialReminderForm,
    type ReminderDate,
    type ReminderForm,
    reminderOptionHint,
    reminderOptionState,
    toReminderRequest,
    validateReminderForm
} from "../reminderForm";
import ReminderOptionRow from "../reminderOptionRow/ReminderOptionRow";
import ReminderSummary from "../reminderSummary/ReminderSummary";
import SpecificDateReminderSection from "../specificDateReminderSection/SpecificDateReminderSection";
import styles from "./NewReminderForm.module.css";

interface NewReminderFormProps {
    task?: TaskDto;
    draft?: NewReminderDraft;
    onDraftSave?: (reminder: TaskInitializeReminderRequest | null) => void;
    close: () => void;
}

const hasReminderOfType = (task: TaskDto | undefined, type: TaskReminderType) =>
    task?.taskReminders?.some(taskReminder => taskReminder.taskReminderType == type) ?? false;

const NewReminderForm: React.FC<NewReminderFormProps> = ({task, draft, onDraftSave, close}) => {
    const {t} = useTranslation();
    const isDraft = onDraftSave != null;
    const [form, setForm] = useState<ReminderForm>(() => initialReminderForm(draft?.reminder));
    const [submitted, setSubmitted] = useState(false);
    const [initializeTaskReminder, {isSuccess, isLoading}] = useInitializeTaskReminderMutation();

    const assignedDate = task ? task.assignedDate : draft?.assignedDate;
    const hasPreciseAssignedDate = task ? task.hasPreciseAssignedDate : draft?.hasPreciseAssignedDate;
    const dueDate = task ? task.dueDate : draft?.dueDate;
    const hasPreciseDueDate = task ? task.hasPreciseDueDate : draft?.hasPreciseDueDate;

    const optionStates = {
        assignedDate: reminderOptionState(assignedDate, hasReminderOfType(task, "ASSIGNED_DATE")),
        dueDate: reminderOptionState(dueDate, hasReminderOfType(task, "DUE_DATE"))
    };
    const assignedDateHint = reminderOptionHint(optionStates.assignedDate, "taskNewReminderHintNoAssignedDate");
    const dueDateHint = reminderOptionHint(optionStates.dueDate, "taskNewReminderHintNoDueDate");

    const request = toReminderRequest(form, optionStates);
    const errors = validateReminderForm(form);
    const canSubmit = isDraft || countDraftReminders(request) != 0;

    useEffect(() => {
        if (isSuccess) {
            close();
        }
    }, [isSuccess]);

    const formatOptionDate = (date: ReminderDate, precise?: boolean) =>
        date ? format(new Date(date), precise ? t("dateTimeFormat") : t("dateFormat")) : null;

    const update = (patch: Partial<ReminderForm>) => {
        setForm(current => ({...current, ...patch}));
    };

    const submit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitted(true);
        if (isLoading || !canSubmit || hasReminderFormErrors(errors)) {
            return;
        }
        if (isDraft) {
            onDraftSave(normalizeDraftReminder(request));
            close();
            return;
        }
        if (task) {
            initializeTaskReminder({taskId: task.taskId, ...request});
        }
    };

    return (
        <form autoComplete="off" id="initialize-task-reminder-form" className={styles.form} onSubmit={submit}>
            <div className={styles.options}>
                <ReminderOptionRow
                    id="reminder-at-assigned-date"
                    label={t("taskNewReminderModalOnAssignedDateLabel")}
                    detail={assignedDateHint ? t(assignedDateHint) : formatOptionDate(assignedDate, hasPreciseAssignedDate)}
                    checked={form.atAssignedDate}
                    disabled={assignedDateHint != null}
                    onChange={atAssignedDate => update({atAssignedDate})}
                />
                <GradientLine/>
                <ReminderOptionRow
                    id="reminder-at-due-date"
                    label={t("taskNewReminderModalOnDueDateLabel")}
                    detail={dueDateHint ? t(dueDateHint) : formatOptionDate(dueDate, hasPreciseDueDate)}
                    checked={form.atDueDate}
                    disabled={dueDateHint != null}
                    onChange={atDueDate => update({atDueDate})}
                />
                <GradientLine/>
                <ReminderOptionRow
                    id="reminder-at-specific-date"
                    label={t("taskNewReminderModalSpecificDateLabel")}
                    checked={form.atSpecificDate}
                    onChange={atSpecificDate => update({atSpecificDate})}
                >
                    <SpecificDateReminderSection form={form} onChange={update} errors={submitted ? errors : {}}/>
                </ReminderOptionRow>
            </div>

            <div className={styles.footer}>
                <GradientLine/>
                <ReminderSummary request={request} assignedDate={assignedDate} dueDate={dueDate}/>
                <div className={styles.buttons}>
                    <Button variant={ButtonVariants.filled} onClick={close}>
                        {t("taskNewReminderCloseButton")}
                    </Button>
                    <Button
                        type="submit"
                        variant={ButtonVariants.contrast}
                        disabled={isLoading || !canSubmit}
                        loading={isLoading}
                    >
                        {isDraft ? t("taskNewReminderAddButton") : t("taskNewReminderCreateButton")}
                    </Button>
                </div>
            </div>
        </form>
    );
};

export default NewReminderForm;
