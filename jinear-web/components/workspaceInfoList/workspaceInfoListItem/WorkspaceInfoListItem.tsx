import Button from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { WorkspaceDto } from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React from "react";
import styles from "./WorkspaceInfoListItem.module.css";

interface WorkspaceInfoListItemProps {
  workspace: WorkspaceDto;
  onWorkspaceChangeComplete?: () => void;
}

const WorkspaceInfoListItem: React.FC<WorkspaceInfoListItemProps> = ({ workspace, onWorkspaceChangeComplete }) => {
  const { t } = useTranslation();
  const router = useRouter();

  const changePrefferedWorkspace = () => {
    router.push(`/${workspace.username}`);
    onWorkspaceChangeComplete?.();
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
