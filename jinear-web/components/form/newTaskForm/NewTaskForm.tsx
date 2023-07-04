import Button, { ButtonVariants } from "@/components/button";
import TaskCreatedToast from "@/components/taskCreatedToast/TaskCreatedToast";
import { TaskInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskMutation } from "@/store/api/taskApi";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import WorkspaceAndTeamInfo from "../common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import styles from "./NewTaskForm.module.css";
import BoardPickerButton from "./boardPickerButton/BoardPickerButton";
import DatePickerButton from "./datePickerButton/DatePickerButton";
import TeamMemberPickerButton from "./teamMemberPickerButton/TeamMemberPickerButton";
import TitleInput from "./titleInput/TitleInput";
import TopicPickerButton from "./topicPickerButton/TopicPickerButton";

interface NewTaskFormProps {
  workspace: WorkspaceDto;
  initialTeam: TeamDto;
  subTaskOf?: string;
  subTaskOfLabel?: string;
  initialAssignedDate?: Date;
  onClose: () => void;
  className?: string;
  footerContainerClass?: string;
}

const logger = Logger("NewTaskForm");

const NewTaskForm: React.FC<NewTaskFormProps> = ({
  workspace,
  initialTeam,
  subTaskOf,
  subTaskOfLabel,
  initialAssignedDate,
  onClose,
  className,
  footerContainerClass,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    reset,
    formState: { errors },
  } = useForm<TaskInitializeRequest>();
  const [selectedTeam, setSelectedTeam] = useState<TeamDto>(initialTeam);
  const workspaceId = workspace.workspaceId;

  const [
    initializeTask,
    { data: initializeTaskResponse, isLoading: isInitializeTaskLoading, isSuccess: isInitializeTaskSuccess },
  ] = useInitializeTaskMutation();

  useEffect(() => {
    reset?.();
    setTimeout(() => {
      setFocus("title");
      setValue("topicId", "no-topic");
      setValue("assignedTo", "no-assignee");
      setValue("boardId", "no-board");
      setValue("teamId", selectedTeam.teamId);
    }, 200);
  }, []);

  useEffect(() => {
    setSelectedTeam(initialTeam);
  }, [initialTeam]);

  useEffect(() => {
    if (selectedTeam) {
      setValue("teamId", selectedTeam.teamId);
    }
  }, [selectedTeam]);

  useEffect(() => {
    if (isInitializeTaskSuccess && initializeTaskResponse) {
      console.log({ NewTaskForm: initializeTaskResponse });
      if (initializeTaskResponse.data.workspace && initializeTaskResponse.data.team) {
        const teamTag = initializeTaskResponse.data.team?.tag;
        const teamTagNo = initializeTaskResponse.data.teamTagNo;
        const tag = `${teamTag}-${teamTagNo}`;
        const workspaceUsername = initializeTaskResponse.data.workspace?.username;
        const taskLink = `${workspaceUsername}/task/${tag}`;
        toast((t) => <TaskCreatedToast teamTaskNo={tag} taskLink={taskLink} />);
      } else {
        toast(t("genericSuccess"));
      }
      reset?.();
      onClose?.();
    }
  }, [isInitializeTaskSuccess, initializeTaskResponse]);

  const submit: SubmitHandler<TaskInitializeRequest> = (data) => {
    if (data.assignedTo == "no-assignee") {
      data.assignedTo = undefined;
    }
    if (data.topicId == "no-topic") {
      data.topicId = undefined;
    }
    if (data.boardId == "no-board") {
      data.boardId = undefined;
    }
    // @ts-ignore
    if (data.assignedDate == "no-date") {
      data.assignedDate = undefined;
    }
    console.log({ NewTaskForm: data });
    if (isInitializeTaskLoading) {
      return;
    }
    initializeTask(data);
  };

  return (
    <form
      autoComplete="off"
      id={"new-task-form"}
      className={cn(styles.form, className)}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={selectedTeam?.teamId} {...register("teamId")} />
        {subTaskOf && <input type="hidden" value={subTaskOf} {...register("subTaskOf")} />}

        {subTaskOfLabel && (
          <div
            dangerouslySetInnerHTML={{ __html: t("newTaskFormSubtaskOfLabel").replace("${subtaskOfLabel}", subTaskOfLabel) }}
          />
        )}

        <TitleInput labelClass={styles.label} register={register} />

        <div className={styles.actionBar}>
          <TopicPickerButton register={register} setValue={setValue} workspace={workspace} team={selectedTeam} />
          {!workspace.isPersonal && (
            <TeamMemberPickerButton register={register} setValue={setValue} teamId={selectedTeam.teamId} />
          )}
          <BoardPickerButton register={register} setValue={setValue} workspace={workspace} team={selectedTeam} />
          <DatePickerButton
            register={register}
            setValue={setValue}
            fieldName="assignedDate"
            isPreciseFieldName="hasPreciseAssignedDate"
            initialAssignedDate={initialAssignedDate}
          />
          <DatePickerButton register={register} setValue={setValue} fieldName="dueDate" isPreciseFieldName="hasPreciseDueDate" />
        </div>

        <WorkspaceAndTeamInfo
          readOnly={subTaskOf != null}
          workspace={workspace}
          team={selectedTeam}
          onTeamChange={setSelectedTeam}
          personalWorkspaceTitle={t("newTaskFormWorkspaceAndTeamInfoForPersonalWorkspaceLabel")}
          workspaceTitle={t("newTaskFormWorkspaceAndTeamInfoLabel")}
          personalWorkspaceLabel={t("newTaskFormPersonalWorkspaceSelected")}
        />
        {/* <DescriptionInput labelClass={styles.label} inputClass={styles.textAreaInput} register={register} setValue={setValue} /> */}
      </div>

      <div className={cn(styles.footerContainer, footerContainerClass)}>
        <Button disabled={isInitializeTaskLoading} onClick={onClose} className={styles.footerButton}>
          {t("newTaskModalCancel")}
        </Button>
        <Button
          type="submit"
          disabled={isInitializeTaskLoading}
          loading={isInitializeTaskLoading}
          className={styles.footerButton}
          variant={ButtonVariants.contrast}
        >
          {t("newTaskModalCreate")}
        </Button>
      </div>
    </form>
  );
};

export default NewTaskForm;
