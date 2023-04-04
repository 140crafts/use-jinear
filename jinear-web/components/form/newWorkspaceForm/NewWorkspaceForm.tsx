import Button, { ButtonVariants } from "@/components/button";
import OrLine from "@/components/orLine/OrLine";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { LocaleType, WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { useInitializeWorkspaceMutation } from "@/store/api/workspaceApi";
import { selectCurrentAccountHasAnyWorkspace, selectCurrentAccountHasAPersonalWorkspace } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { normalizeUsernameReplaceSpaces } from "@/utils/normalizeHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { IoArrowBack, IoCamera } from "react-icons/io5";
import styles from "./NewWorkspaceForm.module.css";
import WorkspaceTypeSelectButton from "./workspaceTypeSelectButton/WorkspaceTypeSelectButton";

interface NewWorkspaceFormProps {
  close?: () => void;
}

const logger = Logger("NewWorkspaceForm");
const USERNAME_REGEX = /[^A-Za-z0-9-_]/;

const NewWorkspaceForm: React.FC<NewWorkspaceFormProps> = ({ close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { register, handleSubmit, setFocus, setValue, watch } = useForm<WorkspaceInitializeRequest>();
  const hasPersonalWorkspace = useTypedSelector(selectCurrentAccountHasAPersonalWorkspace);
  const hasAnyWorkspace = useTypedSelector(selectCurrentAccountHasAnyWorkspace);

  const [isPersonal, setIsPersonal] = useState<boolean>();

  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();
  const photoPickerButtonRef = useRef<HTMLInputElement>(null);

  const currTitle = watch("title");
  const currHandle = watch("handle");

  const [initializeWorkspace, { isLoading, isSuccess }] = useInitializeWorkspaceMutation();

  const submit: SubmitHandler<WorkspaceInitializeRequest> = (data) => {
    logger.log({ data });
    initializeWorkspace({ ...data, locale: t("localeType") as LocaleType });
  };

  useEffect(() => {
    setTimeout(() => {
      setFocus("title");
    }, 100);
  }, []);

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
      close?.();
    }
  }, [isSuccess]);

  const setPersonal = () => {
    setIsPersonal(true);
    setValue("isPersonal", true);
  };
  const setCollaborative = () => {
    setIsPersonal(false);
    setValue("isPersonal", false);
  };
  const unsetType = () => {
    setIsPersonal(undefined);
  };

  const onSearchClick = () => {};

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      setSelectedFile(file);
      return;
    }
    setSelectedFile(undefined);
  };

  useEffect(() => {
    if (!selectedFile) {
      setSelectedFilePreview(undefined);
      return;
    }
    const objectUrl = URL.createObjectURL(selectedFile);
    setSelectedFilePreview(objectUrl);
    return () => URL.revokeObjectURL(objectUrl);
  }, [selectedFile]);

  const pickPhoto = () => {
    logger.log({ selectedFilePreview });
    setSelectedFile(undefined);
    if (photoPickerButtonRef.current) {
      photoPickerButtonRef.current.value = "";
      photoPickerButtonRef.current.click();
    }
  };

  return (
    <form autoComplete="off" id={"new-workspace-form"} className={styles.form} onSubmit={handleSubmit(submit)} action="#">
      <input type={"hidden"} value={`${isPersonal}`} {...register("isPersonal")} />
      {isPersonal == null ? (
        <div className={styles.workspaceTypePickerButtonsContainer}>
          <WorkspaceTypeSelectButton onClick={setPersonal} buttonType={"personal"} />
          <WorkspaceTypeSelectButton onClick={setCollaborative} buttonType={"collaborative"} />
          <OrLine />
          <WorkspaceTypeSelectButton onClick={onSearchClick} buttonType={"search"} />
          <ThemeToggle />
        </div>
      ) : (
        <>
          <Button className={styles.unsetWorkspaceTypeButton} variant={ButtonVariants.outline} onClick={unsetType}>
            <IoArrowBack />
            {t(isPersonal ? "newWorkspaceFormPersonalTitle" : "newWorkspaceFormCollaborativeTitle")}
          </Button>

          <div className={styles.photoTitleContainer}>
            {selectedFile ? (
              <img src={selectedFilePreview} className={styles.profilePicture} onClick={pickPhoto} />
            ) : (
              <Button className={styles.profilePicture} onClick={pickPhoto}>
                <IoCamera size={32} />
              </Button>
            )}
            <input
              ref={photoPickerButtonRef}
              id={"photo-picker"}
              type="file"
              accept="image/*"
              className={styles.photoInput}
              onChange={onSelectFile}
            />

            <label className={cn(styles.label, "flex-1")} htmlFor={"new-workspace-title"}>
              {`${t("newWorkspaceFormWorkspaceTitle")} *`}
              <input id={"new-workspace-title"} type={"text"} {...register("title", { required: t("formRequiredField") })} />
            </label>
          </div>

          <label className={styles.label} htmlFor={"new-workspace-handle"}>
            {t("newWorkspaceFormWorkspaceHandleShort")}
            <div className={styles.urlInputsContainer}>
              <div className={styles.hostInput}>{`${HOST}/`}</div>
              <input
                className={styles.usernameInput}
                id={"new-workspace-handle"}
                type={"text"}
                minLength={1}
                maxLength={255}
                {...register("handle", { required: t("formRequiredField") })}
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
