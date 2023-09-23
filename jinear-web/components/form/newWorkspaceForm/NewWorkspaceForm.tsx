import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { BaseResponse, LocaleType, WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeWorkspaceMutation } from "@/store/api/workspaceApi";
import { useAppDispatch } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { normalizeUsernameReplaceSpaces } from "@/utils/normalizeHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import styles from "./NewWorkspaceForm.module.css";
import NewWorkspaceLogoPicker from "./newWorkspaceLogoPicker/NewWorkspaceLogoPicker";

interface NewWorkspaceFormProps {
  close?: () => void;
  onSuccess?: () => void;
}

const logger = Logger("NewWorkspaceForm");

const NewWorkspaceForm: React.FC<NewWorkspaceFormProps> = ({ close, onSuccess }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { register, handleSubmit, setFocus, setValue, watch } = useForm<WorkspaceInitializeRequest>();
  const handleRef = useRef<HTMLInputElement | null>(null);
  const { ref: handleHookFormRef, ...handleRegisterRest } = register("handle", { required: t("formRequiredField") });

  const [suggestUsername, setSuggestUsername] = useState<boolean>(false);
  const suggestedUsernames = useMemo(
    () => [
      handleRef.current?.value + "-" + `${parseInt(`${Math.random() * 10000}`)}`,
      handleRef.current?.value + "-" + `${parseInt(`${Math.random() * 10000}`)}`,
      handleRef.current?.value + "-" + `${parseInt(`${Math.random() * 10000}`)}`,
      handleRef.current?.value + "-" + `${parseInt(`${Math.random() * 10000}`)}`,
    ],
    [suggestUsername]
  );
  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const currTitle = watch("title");
  const currHandle = watch("handle");

  const [initializeWorkspace, { isLoading, isSuccess, error }] = useInitializeWorkspaceMutation();

  const submit: SubmitHandler<WorkspaceInitializeRequest> = (data) => {
    logger.log({ data });
    let formData = new FormData();
    if (selectedFile) {
      formData.append("logo", selectedFile);
    }
    const request = { ...data, formData };
    initializeWorkspace({ ...request, locale: t("localeType") as LocaleType });
    setSuggestUsername(false);
  };

  useEffect(() => {
    setTimeout(() => {
      setFocus("title");
    }, 250);
  }, []);

  useEffect(() => {
    if (currTitle && currTitle.length > 0) {
      const normalizedUsername = normalizeUsernameReplaceSpaces(currTitle);
      setValue("handle", normalizedUsername?.substring(0, 255));
      setSuggestUsername(false);
    } else {
      setValue("handle", "");
    }
  }, [currTitle]);

  useEffect(() => {
    if (currHandle && currHandle.length > 0) {
      const normalizedUsername = normalizeUsernameReplaceSpaces(currHandle);
      setValue("handle", normalizedUsername?.substring(0, 255));
      setSuggestUsername(false);
    } else {
      setValue("handle", "");
    }
  }, [currHandle]);

  useEffect(() => {
    if (isSuccess) {
      onSuccess?.();
      close?.();
    }
  }, [isSuccess]);

  useEffect(() => {
    logger.log({ error });
    // @ts-ignore
    const err = error?.data as BaseResponse;
    if (err && err.errorCode == "19001") {
      setFocus("handle");
      // @ts-ignore
      handleRef?.current?.select?.();
      setSuggestUsername(true);
    }
  }, [error, handleRef]);

  return (
    <form autoComplete="off" id={"new-workspace-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <div className={styles.photoTitleContainer}>
        <NewWorkspaceLogoPicker
          selectedFile={selectedFile}
          setSelectedFile={setSelectedFile}
          selectedFilePreview={selectedFilePreview}
          setSelectedFilePreview={setSelectedFilePreview}
        />

        <label className={cn(styles.label, "flex-1")} htmlFor={"new-workspace-title"}>
          {`${t("newWorkspaceFormWorkspaceTitle")} *`}
          <input id={"new-workspace-title"} type={"text"} {...register("title", { required: t("formRequiredField") })} />
        </label>
      </div>

      <label className={styles.label} htmlFor={"new-workspace-handle"}>
        {t("newWorkspaceFormWorkspaceHandleShort")}
        <div className={styles.urlInputsContainer}>
          <div className={styles.hostInput}>{`${HOST?.replace("https://", "")?.replace("http://", "")}/`}</div>
          <input
            className={styles.usernameInput}
            id={"new-workspace-handle"}
            type={"text"}
            minLength={1}
            maxLength={255}
            {...handleRegisterRest}
            ref={(e) => {
              handleHookFormRef(e);
              handleRef.current = e;
            }}
          />
        </div>
      </label>

      {suggestUsername && (
        <div>
          <b>Suggested Usernames</b>
          <div className={styles.usernameSuggestionsContainer}>
            {suggestedUsernames.map((username) => (
              <Button
                key={`suggestion-${username}`}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.outline}
                onClick={() => {
                  setValue("handle", username?.substring(0, 255));
                  setSuggestUsername(false);
                }}
              >
                {username}
              </Button>
            ))}
          </div>
        </div>
      )}
      <div className={styles.footerContainer}>
        {close && (
          <Button disabled={isLoading} onClick={close} className={styles.footerButton}>
            {t("newWorkspaceFormCancel")}
          </Button>
        )}
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
