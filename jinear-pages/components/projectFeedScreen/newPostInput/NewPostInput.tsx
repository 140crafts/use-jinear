import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import styles from "./NewPostInput.module.css";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { selectCurrentAccount } from "@/slice/accountSlice";
import ProfilePhoto from "@/components/profilePhoto";
import cn from "classnames";
import { SubmitHandler, useForm } from "react-hook-form";
import { ProjectPostInitializeRequest } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import useTranslation from "@/locals/useTranslation";
import { toast } from "react-hot-toast";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import Line from "@/components/line/Line";
import NewPostFile from "@/components/projectFeedScreen/newPostInput/newPostFile/NewPostFile";
import { useInitializeProjectFeedPostMutation } from "@/api/projectPostApi";
import { useWorkspace } from "@/hooks/useWorkspace";
import { popUpgradeWorkspacePlanModal } from "@/slice/modalSlice";

interface NewPostInputProps {
  projectId: string;
  workspaceId: string;
}

const logger = Logger("NewPostInput");

const NewPostInput: React.FC<NewPostInputProps> = ({ projectId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const attachmentPickerRef = useRef<HTMLInputElement>(null);
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const workspace = useWorkspace(workspaceId);
  const canAttachFiles = workspace && workspace.tier != "BASIC";

  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<ProjectPostInitializeRequest>();
  const [files, setFiles] = useState<File[]>([]);
  const bodyRef = useRef<ITiptapRef>(null);
  const [initializeProjectFeedPost, { data: initializeResponse, isLoading }] = useInitializeProjectFeedPostMutation();

  useEffect(() => {
    if (initializeResponse) {
      bodyRef.current && bodyRef.current.clearContent();
      setFiles([]);
    }
  }, [initializeResponse, bodyRef]);

  const onSelectFile = (event: ChangeEvent<HTMLInputElement>) => {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      const file = event.target?.files?.[0];
      if (file) {
        const fileSize = file?.size || 0;
        if (fileSize / 1000 / 1000 > 32) {
          toast(t("apiFileTooLargeError"));
          return;
        }
        setFiles([...files, file]);
      }
      return;
    }
  };

  const pickAttachment = () => {
    if (!canAttachFiles && workspace) {
      dispatch(popUpgradeWorkspacePlanModal({ workspaceId: workspace.workspaceId, visible: true }));
      return;
    }
    if (attachmentPickerRef.current) {
      attachmentPickerRef.current.value = "";
      attachmentPickerRef.current.click();
    }
  };

  const removePickedFile = (index: number) => {
    const next: File[] = [];
    files.forEach((f, i) => {
      if (i != index) {
        next.push(f);
      }
    });
    setFiles(next);
  };

  const submit: SubmitHandler<ProjectPostInitializeRequest> = (data) => {
    logger.log({ submit: data });
    if (bodyRef.current) {
      const formData = new FormData();
      const body = bodyRef.current.getHTML();
      formData.append("projectId", projectId);
      formData.append("body", body);
      files.forEach(file => formData.append("files", file));

      initializeProjectFeedPost({ formData });
    }
  };

  return (
    <>
      <Line />
      <div className={styles.container}>
        <h2>Post a update</h2>
        <div className={styles.contentContainer}>
          <div className={styles.profilePicContainer}>
            <ProfilePhoto
              boringAvatarKey={currentAccount?.accountId || ""}
              url={currentAccount?.profilePicture?.url}
              wrapperClassName={styles.profilePic}
            />
          </div>
          <form
            autoComplete="off"
            id={"feed-new-post-form"}
            className={cn(styles.form)}
            onSubmit={handleSubmit(submit)}
            action="#"
          >
            <input type={"hidden"} value={projectId} {...register("projectId")} />

            <Tiptap
              ref={bodyRef}
              htmlInputId={"new-project-post-description"}
              className={styles.input}
              editorClassName={styles.input}
              editorWrapperClassName={styles.editorWrapper}
              placeholder={t("projectPostNewUpdatePlaceholder")}
              editable={!isLoading}
              hideActionBarWhenEmpty={false}
              workspaceIdForImages={workspaceId}
            />

            <input
              ref={attachmentPickerRef}
              id={"post-photo-picker"}
              type="file"
              accept="*/*"
              className={styles.attachmentInput}
              onChange={onSelectFile}
            />


            <div className={styles.fileListContainer}>
              {files.map((file, i) =>
                <NewPostFile
                  key={`selected-file-list-${i}`}
                  file={file}
                  onRemove={() => removePickedFile(i)}
                />)}
            </div>

            <div className={styles.buttonContainer}>
              <Button
                disabled={isLoading}
                variant={ButtonVariants.filled}
                heightVariant={ButtonHeight.short}
                onClick={pickAttachment}
                type={"button"}
              >
                {t("projectPostNewUpdateAddFile")}
              </Button>

              <Button
                variant={ButtonVariants.contrast}
                heightVariant={ButtonHeight.short}
                type={"submit"}
                disabled={isLoading}
                loading={isLoading}
              >
                {t("projectPostNewUpdatePost")}
              </Button>
            </div>

          </form>
        </div>
      </div>
    </>
  );
};

export default NewPostInput;