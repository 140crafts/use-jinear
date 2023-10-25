import Button from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./WorkspaceInfoListItem.module.css";

interface WorkspaceInfoListItemProps {
  workspace: WorkspaceDto;
  onWorkspaceChangeComplete?: () => void;
}

const WorkspaceInfoListItem: React.FC<WorkspaceInfoListItemProps> = ({ workspace, onWorkspaceChangeComplete }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [updatePreferredWorkspace, { isLoading, isSuccess }] = useUpdatePreferredWorkspaceMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isLoading }));
    if (!isLoading && isSuccess) {
      onWorkspaceChangeComplete?.();
    }
  }, [isLoading, isSuccess]);

  const changePrefferedWorkspace = () => {
    updatePreferredWorkspace({ workspaceId: workspace.workspaceId });
  };

  return (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <div
          className={cn(
            styles.profilePicContainer,
            !workspace?.profilePicture?.storagePath && styles["profilePicContainer-without-profile-pic"]
          )}
        >
          {workspace?.profilePicture?.storagePath ? (
            <ProfilePhoto
              boringAvatarKey={workspace.workspaceId}
              storagePath={workspace?.profilePicture?.storagePath}
              wrapperClassName={styles.profilePic}
              imgClassName={styles.profilePicImg}
            />
          ) : (
            <div className={styles.firstLetter}>{workspace.username.substring(0, 1)?.toLocaleUpperCase()}</div>
          )}
        </div>
        <Button className={styles.nameContainer} onClick={changePrefferedWorkspace}>
          <span className={cn(styles.title, "line-clamp-2")}>{workspace?.title}</span>
          <div className="flex-1" />
          <div>{"->"}</div>
        </Button>
      </div>
    </div>
  );
};

export default WorkspaceInfoListItem;
