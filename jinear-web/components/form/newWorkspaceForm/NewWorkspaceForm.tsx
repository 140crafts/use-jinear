import Button, { ButtonVariants } from "@/components/button";
import { BaseResponse, LocaleType, WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeWorkspaceMutation } from "@/store/api/workspaceApi";
import { selectCurrentAccountHasAPersonalWorkspace } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { normalizeUsernameReplaceSpaces } from "@/utils/normalizeHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { IoArrowBack } from "react-icons/io5";
import styles from "./NewWorkspaceForm.module.css";
import NewWorkspaceLogoPicker from "./newWorkspaceLogoPicker/NewWorkspaceLogoPicker";
import WorkspaceTypeSelect from "./workspaceTypeSelect/WorkspaceTypeSelect";

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

  const hasPersonalWorkspace = useTypedSelector(selectCurrentAccountHasAPersonalWorkspace);

  const [isPersonal, setIsPersonal] = useState<boolean>();

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
  };

  useEffect(() => {
    if (isPersonal != null) {
      setTimeout(() => {
        setFocus("title");
      }, 100);
    }
  }, [isPersonal]);

  useEffect(() => {
    if (currTitle && currTitle.length > 0) {
      const normalizedUsername = normalizeUsernameReplaceSpaces(currTitle);
      setValue("handle", normalizedUsername?.substring(0, 255));
    } else {
      setValue("handle", "");
    }
  }, [currTitle]);

  useEffect(() => {
    if (currHandle && currHandle.length > 0) {
      const normalizedUsername = normalizeUsernameReplaceSpaces(currHandle);
      setValue("handle", normalizedUsername?.substring(0, 255));
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
    }
  }, [error, handleRef]);

  const unsetType = () => {
    setIsPersonal(undefined);
  };

  return (
    <form autoComplete="off" id={"new-workspace-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <input type={"hidden"} value={`${isPersonal}`} {...register("isPersonal")} />
      {isPersonal == null ? (
        <WorkspaceTypeSelect hasPersonalWorkspace={hasPersonalWorkspace} setIsPersonal={setIsPersonal} setValue={setValue} />
      ) : (
        <>
          <Button className={styles.unsetWorkspaceTypeButton} variant={ButtonVariants.outline} onClick={unsetType}>
            <IoArrowBack />
            {t(isPersonal ? "newWorkspaceFormPersonalTitle" : "newWorkspaceFormCollaborativeTitle")}
          </Button>

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
        </>
      )}
    </form>
  );
};

export default NewWorkspaceForm;
