import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskListInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskListMutation } from "@/store/api/taskListApi";
import { changeLoadingModalVisibility, closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { IoClose } from "react-icons/io5";
import WorkspaceAndTeamInfo from "../common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import styles from "./NewTaskListForm.module.css";

interface NewTaskListFormProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  onClose: () => void;
}

const logger = Logger("NewTaskListForm");

const NewTaskListForm: React.FC<NewTaskListFormProps> = ({ workspace, team, onClose }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [date, setDate] = useState<Date>();

  const [initializeTaskList, { isLoading: isInitializeTaskListLoading, isSuccess }] = useInitializeTaskListMutation();

  const { register, handleSubmit, setFocus, setValue, watch } = useForm<TaskListInitializeRequest>();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isInitializeTaskListLoading }));
    if (isSuccess) {
      onClose?.();
    }
  }, [isInitializeTaskListLoading, isSuccess]);

  const submit: SubmitHandler<TaskListInitializeRequest> = (data) => {
    logger.log({ data });
    initializeTaskList(data);
  };

  const clearDueDate = () => {
    setDate(undefined);
  };

  const onDateChange = (date: Date) => {
    dispatch(closeDatePickerModal());
    setDate(date);
  };

  const popDatePickerForDueDate = () => {
    dispatch(popDatePickerModal({ visible: true, initialDate: date ? new Date(date) : new Date(), onDateChange }));
  };

  return (
    <form autoComplete="off" id={"new-task-list-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <div className={styles.formContent}>
        <WorkspaceAndTeamInfo
          workspace={workspace}
          team={team}
          personalWorkspaceTitle={t("newTaskListFormWorkspaceAndTeamInfoForPersonalWorkspaceLabel")}
          workspaceTitle={t("newTaskListFormWorkspaceAndTeamInfoLabel")}
          personalWorkspaceLabel={t("newTaskListFormPersonalWorkspaceSelected")}
        />
        <input type="hidden" value={workspace.workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={team.teamId} {...register("teamId")} />
        {date && <input type="hidden" value={date?.toISOString?.()} {...register("dueDate")} />}

        <label className={styles.label} htmlFor={"new-task-list-title"}>
          {`${t("newTaskListModalTaskListTitle")} *`}
          <input id={"new-task-list-title"} type={"text"} {...register("title", { required: t("formRequiredField") })} />
        </label>

        <div className={styles.label}>
          {t("newTaskListModalTaskListDueDate")}
          <div className={styles.dateButtonContainer}>
            {date && (
              <Button onClick={clearDueDate}>
                <IoClose />
              </Button>
            )}
            <Button variant={ButtonVariants.filled} onClick={popDatePickerForDueDate} heightVariant={ButtonHeight.short}>
              {date ? format(date, t("dateFormat")) : t("datePickerSelectDate")}
            </Button>
          </div>
        </div>

        <div className={styles.footerContainer}>
          <Button disabled={isInitializeTaskListLoading} onClick={onClose} className={styles.footerButton}>
            {t("newTaskListModalCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInitializeTaskListLoading}
            loading={isInitializeTaskListLoading}
            className={styles.footerButton}
            variant={ButtonVariants.contrast}
          >
            {t("newTaskListModalCreate")}
          </Button>
        </div>
      </div>
    </form>
  );
};

export default NewTaskListForm;
