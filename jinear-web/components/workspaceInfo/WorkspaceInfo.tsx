import { WorkspaceDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import ProfilePhoto from "../profilePhoto";
import styles from "./WorkspaceInfo.module.css";

interface WorkspaceInfoProps {
  workspace: WorkspaceDto;
}

const WorkspaceInfo: React.FC<WorkspaceInfoProps> = ({ workspace }) => {
  return (
    <div className={styles.container}>
      <div
        className={cn(
          styles.profilePicContainer,
          !workspace?.profilePicture?.url && styles["profilePicContainer-without-profile-pic"]
        )}
      >
        {workspace?.profilePicture?.url ? (
          <ProfilePhoto
            boringAvatarKey={workspace.workspaceId}
            url={workspace?.profilePicture?.url}
            wrapperClassName={styles.profilePic}
            imgClassName={styles.profilePicImg}
          />
        ) : (
          <div className={styles.firstLetter}>{workspace.username.substring(0, 1)?.toLocaleUpperCase()}</div>
        )}
      </div>
      <div className={styles.infoContainer}>
        <span className={cn(styles.title, "line-clamp-2")}>{workspace?.title}</span>
        {/* <span>
          <Link target="_blank" href={`${HOST}/${workspace?.username}`}>
            {`${HOST?.replace("https://", "")?.replace("http://", "")}/${workspace?.username}`}
          </Link>
        </span> */}
      </div>
    </div>
  );
};

export default WorkspaceInfo;
