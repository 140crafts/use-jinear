import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeWorkspaceMutation } from "@/store/api/workspaceApi";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./NewWorkspaceForm.module.css";

interface NewWorkspaceFormProps {
  close?: () => void;
}

const logger = Logger("NewWorkspaceForm");

const NewWorkspaceForm: React.FC<NewWorkspaceFormProps> = ({ close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { register, handleSubmit, setFocus, setValue, watch } =
    useForm<WorkspaceInitializeRequest>();
  const currTitle = watch("title");

  const [initializeWorkspace, { isLoading, isSuccess }] =
    useInitializeWorkspaceMutation();

  const submit: SubmitHandler<WorkspaceInitializeRequest> = (data) => {
    logger.log({ data });
    initializeWorkspace(data);
  };

  useEffect(() => {
    setTimeout(() => {
      setFocus("title");
    }, 100);
  }, []);

  useEffect(() => {
    if (currTitle && currTitle.length > 0) {
      setValue("handle", currTitle.substring(0, 3)?.toLocaleUpperCase("en-US"));
    }
  }, [currTitle]);

  useEffect(() => {
    if (isSuccess) {
      close?.();
    }
  }, [isSuccess]);

  return (
    <form
      autoComplete="off"
      id={"new-workspace-form"}
      className={styles.form}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.titleContainer}>
        <label
          className={cn(styles.label, "flex-1")}
          htmlFor={"new-workspace-title"}
        >
          {`${t("newWorkspaceFormWorkspaceTitle")} *`}
          <input
            id={"new-workspace-title"}
            type={"text"}
            {...register("title", { required: t("formRequiredField") })}
          />
        </label>
        <label
          className={styles.label}
          htmlFor={"new-workspace-handle"}
          data-tooltip-new-workspace-handle={t(
            "newWorkspaceFormWorkspaceHandle"
          )}
        >
          {`${t("newWorkspaceFormWorkspaceHandleShort")} *`}
          <input
            id={"new-workspace-handle"}
            type={"text"}
            className={styles.handleInput}
            minLength={1}
            maxLength={255}
            {...register("handle", { required: t("formRequiredField") })}
          />
        </label>
      </div>
      <label className={styles.label} htmlFor={"new-workspace-description"}>
        {`${t("newWorkspaceFormWorkspaceDescription")}`}
        <textarea
          id={"new-workspace-description"}
          rows={4}
          {...register("description")}
        />
      </label>

      <div className={styles.footerContainer}>
        <Button
          disabled={isLoading}
          onClick={close}
          className={styles.footerButton}
        >
          {t("newWorkspaceFormCancel")}
        </Button>
        <Button
          type="submit"
          disabled={isLoading}
          loading={isLoading}
          className={styles.footerButton}
          variant={ButtonVariants.contrast}
        >
          {t("newWorkspaceFormCreate")}
        </Button>
      </div>
    </form>
  );
};

export default NewWorkspaceForm;
