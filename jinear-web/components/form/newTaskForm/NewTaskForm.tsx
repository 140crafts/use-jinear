import Button, { ButtonVariants } from "@/components/button";
import TaskCreatedToast from "@/components/taskCreatedToast/TaskCreatedToast";
import { TaskInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskMutation } from "@/store/api/taskApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import styles from "./NewTaskForm.module.css";
import AssignedDateInput from "./assignedDateInput/AssignedDateInput";
import DescriptionInput from "./descriptionInput/DescriptionInput";
import DueDateInput from "./dueDateInput/DueDateInput";
import MemberSelect from "./memberSelect/MemberSelect";
import TitleInput from "./titleInput/TitleInput";
import TopicSelect from "./topicSelect/TopicSelect";
import WorkspaceAndTeamInfo from "./workspaceAndTeamInfo/WorkspaceAndTeamInfo";

interface NewTaskFormProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  subTaskOf?: string;
  subTaskOfLabel?: string;
  onClose: () => void;
  className?: string;
}

export interface NewTaskExtendedForm extends TaskInitializeRequest {
  assignedDate_ISO?: string;
  dueDate_ISO?: string;
}

const logger = Logger("NewTaskForm");

const NewTaskForm: React.FC<NewTaskFormProps> = ({ workspace, team, subTaskOf, subTaskOfLabel, onClose, className }) => {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    reset,
    formState: { errors },
    watch,
  } = useForm<NewTaskExtendedForm>();
  const workspaceId = workspace.workspaceId;
  const teamId = team.teamId;

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
      setValue("teamId", teamId);
    }, 200);
  }, []);

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

  const submit: SubmitHandler<NewTaskExtendedForm> = (data) => {
    if (data.assignedTo == "no-assignee") {
      data.assignedTo = undefined;
    }
    if (data.topicId == "no-topic") {
      data.topicId = undefined;
    }
    if (data.assignedDate_ISO) {
      data.assignedDate = new Date(data.assignedDate_ISO);
    }
    if (data.dueDate_ISO) {
      data.dueDate = new Date(data.dueDate_ISO);
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
        <WorkspaceAndTeamInfo workspace={workspace} team={team} />
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={teamId} {...register("teamId")} />
        {subTaskOf && <input type="hidden" value={subTaskOf} {...register("subTaskOf")} />}

        {subTaskOfLabel && (
          <div
            dangerouslySetInnerHTML={{ __html: t("newTaskFormSubtaskOfLabel").replace("${subtaskOfLabel}", subTaskOfLabel) }}
          />
        )}

        <TitleInput labelClass={styles.label} register={register} />

        <DescriptionInput labelClass={styles.label} inputClass={styles.textAreaInput} register={register} setValue={setValue} />

        <AssignedDateInput labelClass={styles.label} register={register} watch={watch} setValue={setValue} />
        <DueDateInput labelClass={styles.label} register={register} watch={watch} setValue={setValue} />

        <TopicSelect
          teamId={teamId}
          register={register}
          setValue={setValue}
          labelClass={styles.label}
          loadingClass={styles.loadingContainer}
          selectClass={styles.select}
        />

        <MemberSelect
          teamId={teamId}
          register={register}
          setValue={setValue}
          labelClass={styles.label}
          loadingClass={styles.loadingContainer}
          selectClass={styles.select}
        />
      </div>

      <div className={styles.footerContainer}>
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
