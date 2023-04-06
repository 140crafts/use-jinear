import Button from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import cn from "classnames";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./WorkspaceButton.module.css";

interface WorkspaceButtonProps {
  workspace: WorkspaceDto;
}

const WorkspaceButton: React.FC<WorkspaceButtonProps> = ({ workspace }) => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const prefferedWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const isPreffered = workspace.workspaceId == prefferedWorkspaceId;
  const [updatePreferredWorkspace, { isLoading, isSuccess }] = useUpdatePreferredWorkspaceMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isLoading || isSuccess }));
    if (isSuccess) {
      // router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isSuccess, isLoading]);

  const changePrefferedWorkspace = () => {
    if (!isPreffered) {
      //to avoid WorkspaceAndTeamChangeListener kick in when user is at task page
      router.replace(ROUTE_IF_LOGGED_IN);
      updatePreferredWorkspace({ workspaceId: workspace.workspaceId });
    }
  };

  return (
    <div className={cn(styles.wrapper, isPreffered && styles.preffered)}>
      <Button
        onClick={changePrefferedWorkspace}
        className={cn(styles.container, !workspace?.profilePicture?.storagePath && styles["container-without-profile-pic"])}
        data-tooltip={workspace.username}
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
      </Button>
    </div>
  );
};

export default WorkspaceButton;
