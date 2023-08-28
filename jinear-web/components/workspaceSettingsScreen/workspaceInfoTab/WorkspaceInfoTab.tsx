import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import { LocaleType, WorkspaceDto } from "@/model/be/jinear-core";
import { s3Base } from "@/store/api/api";
import { useUpdateWorkspaceProfilePictureMutation } from "@/store/api/workspaceMediaApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import WorkspaceLogoPicker from "../workspaceLogoPicker/WorkspaceLogoPicker";
import styles from "./WorkspaceInfoTab.module.scss";

interface WorkspaceInfoTabProps {
  workspace: WorkspaceDto;
}

const logger = Logger("WorkspaceInfoTab");

const WorkspaceInfoTab: React.FC<WorkspaceInfoTabProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const [updateWorkspaceProfilePicture, { isSuccess, isLoading, isError }] = useUpdateWorkspaceProfilePictureMutation();
  const workspaceTier = workspace.tier;
  const workspaceTierLabel = workspace.isPersonal
    ? t("workspaceInfoTabWorkspaceType_Personal")
    : t(`workspaceInfoTabWorkspaceType_Collaborative_${workspaceTier}`);
  const workspaceTierDetailLabel = workspace.isPersonal
    ? t("workspaceInfoTabWorkspaceType_Personal_detail")
    : t(`workspaceInfoTabWorkspaceType_Collaborative_${workspaceTier}_detail`);

  useEffect(() => {
    if (selectedFile && workspace) {
      logger.log({ selectedFile });
      let formData = new FormData();
      if (selectedFile) {
        formData.append("file", selectedFile);
      }
      const request = { workspaceId: workspace.workspaceId, formData };
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
          currentPhotoPath={workspace?.profilePicture?.storagePath ? s3Base + workspace?.profilePicture?.storagePath : undefined}
        />

        <div className={styles.infoContainer}>
          <h2 className={cn(styles.title, "line-clamp-2")}>{workspace?.title}</h2>

          <h3>
            <Link target="_blank" href={`${HOST}/${workspace?.username}`}>
              {`${HOST?.replace("https://", "")?.replace("http://", "")}/${workspace?.username}`}
            </Link>
          </h3>

          <div className={styles.workspaceTierContainer}>
            <div className={styles.workspaceTierLabelContainer}>
              <h3>{workspaceTierLabel}</h3>
              <span>{workspaceTierDetailLabel}</span>
            </div>
            <WorkspaceUpgradeButton workspace={workspace} variant={"FULL"} className={styles.upgradeButton} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default WorkspaceInfoTab;
