import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskBoardInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskBoardMutation } from "@/store/api/taskBoardApi";
import { changeLoadingModalVisibility, closeDatePickerModal, popDatePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { IoClose } from "react-icons/io5";
import WorkspaceAndTeamInfo from "../common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import styles from "./NewTaskBoardForm.module.css";

interface NewTaskBoardFormProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  onClose: () => void;
}

const logger = Logger("NewTaskBoardForm");

const NewTaskBoardForm: React.FC<NewTaskBoardFormProps> = ({ workspace, team, onClose }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [date, setDate] = useState<Date>();

  const [initializeTaskBoard, { isLoading: isInitializeTaskBoardLoading, isSuccess }] = useInitializeTaskBoardMutation();

  const { register, handleSubmit, setFocus, setValue, watch } = useForm<TaskBoardInitializeRequest>();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isInitializeTaskBoardLoading }));
    if (isSuccess) {
      onClose?.();
    }
  }, [isInitializeTaskBoardLoading, isSuccess]);

  const submit: SubmitHandler<TaskBoardInitializeRequest> = (data) => {
    logger.log({ data });
    initializeTaskBoard(data);
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
    <form autoComplete="off" id={"new-task-board-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <div className={styles.formContent}>
        <input type="hidden" value={workspace.workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={team.teamId} {...register("teamId")} />
        {date && <input type="hidden" value={date?.toISOString?.()} {...register("dueDate")} />}

        <label className={styles.label} htmlFor={"new-task-board-title"}>
          <b>{`${t("newTaskListModalTaskListTitle")} *`}</b>
          <input id={"new-task-board-title"} type={"text"} {...register("title", { required: t("formRequiredField") })} />
        </label>

        <div className={styles.dateButtonContainer}>
          {date && (
            <Button onClick={clearDueDate}>
              <IoClose />
            </Button>
          )}
          <Button variant={ButtonVariants.filled} onClick={popDatePickerForDueDate} heightVariant={ButtonHeight.short}>
            {date ? format(date, t("dateFormat")) : t("newTaskListModalTaskListDueDate")}
          </Button>
        </div>
        <WorkspaceAndTeamInfo
          readOnly
          workspace={workspace}
          team={team}
          workspaceTitle={t("newTaskListFormWorkspaceAndTeamInfoLabel")}
        />
        <div className={styles.footerContainer}>
          <Button disabled={isInitializeTaskBoardLoading} onClick={onClose} className={styles.footerButton}>
            {t("newTaskListModalCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInitializeTaskBoardLoading}
            loading={isInitializeTaskBoardLoading}
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

export default NewTaskBoardForm;
