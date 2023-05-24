import { LocaleType } from "@/model/be/jinear-core";
import { s3Base } from "@/store/api/api";
import { useUpdateWorkspaceProfilePictureMutation } from "@/store/api/workspaceMediaApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import WorkspaceLogoPicker from "../workspaceLogoPicker/WorkspaceLogoPicker";
import styles from "./WorkspaceInfoTab.module.css";

interface WorkspaceInfoTabProps {}

const logger = Logger("WorkspaceInfoTab");

const WorkspaceInfoTab: React.FC<WorkspaceInfoTabProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const selectedWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const [updateWorkspaceProfilePicture, { isSuccess, isLoading, isError }] = useUpdateWorkspaceProfilePictureMutation();

  useEffect(() => {
    if (selectedFile && selectedWorkspace) {
      logger.log({ selectedFile });
      let formData = new FormData();
      if (selectedFile) {
        formData.append("file", selectedFile);
      }
      const request = { workspaceId: selectedWorkspace.workspaceId, formData };
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateWorkspaceProfilePicture({ ...request, locale: t("localeType") as LocaleType });
    }
  }, [selectedFile]);

  return (
    <div className={styles.container}>
      <div className={styles.profileContainer}>
        <WorkspaceLogoPicker
          selectedFile={selectedFile}
          setSelectedFile={setSelectedFile}
          selectedFilePreview={selectedFilePreview}
          setSelectedFilePreview={setSelectedFilePreview}
          currentPhotoPath={
            selectedWorkspace?.profilePicture?.storagePath ? s3Base + selectedWorkspace?.profilePicture?.storagePath : undefined
          }
        />
        <div className={styles.infoContainer}>
          <h2 className={cn(styles.title, "line-clamp-2")}>{selectedWorkspace?.title}</h2>
          <h3>
            <Link target="_blank" href={`${HOST}/${selectedWorkspace?.username}`}>
              {`${HOST?.replace("https://", "")?.replace("http://", "")}/${selectedWorkspace?.username}`}
            </Link>
          </h3>
        </div>
      </div>
    </div>
  );
};

export default WorkspaceInfoTab;
