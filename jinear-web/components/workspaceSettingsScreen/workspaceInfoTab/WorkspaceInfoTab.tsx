import WorkspaceSubscriptionInfo from "@/components/workspaceSubscriptionInfo/WorkspaceSubscriptionInfo";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import { LocaleType, WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdateWorkspaceProfilePictureMutation } from "@/store/api/workspaceMediaApi";
import {
  changeLoadingModalVisibility,
  closeBasicTextInputModal,
  popBasicTextInputModal
} from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { HOST } from "@/utils/constants";
import Logger from "@/utils/logger";
import { isWorkspaceInPaidTier } from "@/utils/permissionHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import Link from "next/link";
import React, { useEffect, useState } from "react";
import WorkspaceLogoPicker from "../workspaceLogoPicker/WorkspaceLogoPicker";
import styles from "./WorkspaceInfoTab.module.scss";
import { useWorkspaceRole } from "@/hooks/useWorkspaceRole";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPencil } from "react-icons/lu";
import { useUpdateWorkspaceTitleMutation } from "@/api/workspaceApi";

interface WorkspaceInfoTabProps {
  workspace: WorkspaceDto;
}

const logger = Logger("WorkspaceInfoTab");

const WorkspaceInfoTab: React.FC<WorkspaceInfoTabProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const workspaceRole = useWorkspaceRole({ workspaceId: workspace.workspaceId }) || "";
  const hasEditAccess = ["OWNER", "ADMIN"].indexOf(workspaceRole) != -1;

  const [selectedFile, setSelectedFile] = useState<File | undefined>();
  const [selectedFilePreview, setSelectedFilePreview] = useState<string | undefined>();

  const [updateWorkspaceProfilePicture, { isSuccess, isLoading, isError }] = useUpdateWorkspaceProfilePictureMutation();
  const workspaceTier = workspace.tier;
  const workspaceTierLabel = t(`workspaceInfoTabWorkspaceType_Collaborative_${workspaceTier}`);
  const workspaceTierDetailLabel = t(`workspaceInfoTabWorkspaceType_Collaborative_${workspaceTier}_detail`);

  const [updateWorkspaceTitle, { isLoading: isUpdateWorkspaceTitleLoading }] = useUpdateWorkspaceTitleMutation();

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

  const popNewWorkspaceNamePicker = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("updateWorkspaceTitleModalTitle"),
        infoText: t("updateWorkspaceTextModalText"),
        onSubmit: onUpdateTitle
      })
    );
  };

  const onUpdateTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    updateWorkspaceTitle({ workspaceId: workspace.workspaceId, body: { title } });
  };

  return (
    <div className={styles.container}>
      <div className={styles.profileContainer}>
        <WorkspaceLogoPicker
          disabled={!hasEditAccess}
          selectedFile={selectedFile}
          setSelectedFile={setSelectedFile}
          selectedFilePreview={selectedFilePreview}
          setSelectedFilePreview={setSelectedFilePreview}
          currentPhotoPath={workspace?.profilePicture?.url ? workspace?.profilePicture?.url : undefined}
        />

        <div className={styles.infoContainer}>
          {hasEditAccess &&
            <Button
              disabled={isUpdateWorkspaceTitleLoading}
              onClick={popNewWorkspaceNamePicker}
              className={styles.editWorkspaceNameButton}
              variant={ButtonVariants.contrast}
              heightVariant={ButtonHeight.short}
              data-tooltip-right={t("projectFeedInfoEdit")}
            >
              <LuPencil />
            </Button>
          }

          <div>
            <h2 className={cn(styles.title, "line-clamp-2")}>{workspace?.title}</h2>
            <h3>
              <Link target="_blank" href={`${HOST}/${workspace?.username}`}>
                {`${HOST?.replace("https://", "")?.replace("http://", "")}/${workspace?.username}`}
              </Link>
            </h3>
          </div>

          <div className={styles.workspaceTierContainer}>
            <div className={styles.workspaceTierLabelContainer}>
              <h3>{workspaceTierLabel}</h3>
              <span>{workspaceTierDetailLabel}</span>
            </div>

            <WorkspaceUpgradeButton workspace={workspace} variant={"FULL"} className={styles.upgradeButton} />
            {workspace && isWorkspaceInPaidTier(workspace) &&
              <WorkspaceSubscriptionInfo workspaceId={workspace.workspaceId} />}
          </div>
        </div>
      </div>
    </div>
  );
};

export default WorkspaceInfoTab;
