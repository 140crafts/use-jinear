import Button, { ButtonVariants } from "@/components/button";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { closeNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
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
}

const logger = Logger("NewTaskForm");

const NewTaskForm: React.FC<NewTaskFormProps> = ({ workspaceId, teamId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const {
    register,
    reset,
    watch,
    handleSubmit,
    setFocus,
    setValue,
    formState: { errors },
  } = useForm<TaskInitializeRequest>();

  useEffect(() => {
    setTimeout(() => {
      setFocus("title");
      setValue("assignedTo", "no-assignee");
      setValue("teamId", teamId);
    }, 250);
  }, []);

  const close = () => {
    dispatch(closeNewTaskModal());
  };

  const submit: SubmitHandler<TaskInitializeRequest> = (data) => {
    // if (isLoading) {
    //   return;
    // }
    // if (data.newPassword != data.newPasswordControl) {
    //   toast(t("changePasswordFormPasswordsNotMatch"));
    //   return;
    // }
    // updatePassword(data);
  };

  return (
    <form
      autoComplete="off"
      id={"new-task-form"}
      className={styles.form}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={teamId} {...register("teamId")} />

        <TitleInput labelClass={styles.label} register={register} />

        <DescriptionInput
          labelClass={styles.label}
          inputClass={styles.textAreaInput}
          register={register}
        />

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
        <Button onClick={close} className={styles.footerButton}>
          {t("newTaskModalCancel")}
        </Button>
        <Button
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
