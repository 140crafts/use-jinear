import Button, { ButtonVariants } from "@/components/button";
import TaskCreatedToast from "@/components/taskCreatedToast/TaskCreatedToast";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeTaskMutation } from "@/store/api/taskApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import { parse } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import AssignedDateInput from "./assignedDateInput/AssignedDateInput";
import DescriptionInput from "./descriptionInput/DescriptionInput";
import DueDateInput from "./dueDateInput/DueDateInput";
import MemberSelect from "./memberSelect/MemberSelect";
import styles from "./NewTaskForm.module.css";
import TitleInput from "./titleInput/TitleInput";
import TopicSelect from "./topicSelect/TopicSelect";

interface NewTaskFormProps {
  workspaceId: string;
  teamId: string;
  subTaskOf?: string;
  onClose: () => void;
  className?: string;
}

const logger = Logger("NewTaskForm");

const NewTaskForm: React.FC<NewTaskFormProps> = ({ workspaceId, teamId, subTaskOf, onClose, className }) => {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    formState: { errors },
  } = useForm<TaskInitializeRequest>();

  const [
    initializeTask,
    { data: initializeTaskResponse, isLoading: isInitializeTaskLoading, isSuccess: isInitializeTaskSuccess },
  ] = useInitializeTaskMutation();

  useEffect(() => {
    setTimeout(() => {
      setFocus("title");
      setValue("assignedTo", "no-assignee");
      setValue("teamId", teamId);
    }, 250);
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
    if (data.assignedDate) {
      //@ts-ignore
      data.assignedDate = parse(data.assignedDate, "yyyy-MM-dd", new Date());
    }
    if (data.dueDate) {
      //@ts-ignore
      data.dueDate = parse(data.dueDate, "yyyy-MM-dd", new Date());
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
        <input type="hidden" value={teamId} {...register("teamId")} />
        {subTaskOf && <input type="hidden" value={subTaskOf} {...register("subTaskOf")} />}

        <TitleInput labelClass={styles.label} register={register} />

        <DescriptionInput labelClass={styles.label} inputClass={styles.textAreaInput} register={register} setValue={setValue} />

        <div className={styles.dateInputContainer}>
          <AssignedDateInput labelClass={styles.label} register={register} />
          <DueDateInput labelClass={styles.label} register={register} />
        </div>

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
