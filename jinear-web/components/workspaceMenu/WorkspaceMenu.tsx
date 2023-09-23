import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { popNewWorkspaceModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { HiPlus } from "react-icons/hi";
import Button, { ButtonVariants } from "../button";
import styles from "./WorkspaceMenu.module.css";
import WorkspaceButton from "./workspaceButton/WorkspaceButton";

interface WorkspaceMenuProps {}

const WorkspaceMenu: React.FC<WorkspaceMenuProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const workspaces = useTypedSelector(selectCurrentAccountsWorkspaces);

  const onNewWorkspaceButtonClick = () => {
    dispatch(popNewWorkspaceModal());
  };

  return (
    <div className={styles.container}>
      <div className={styles.workspaceList}>
        {workspaces?.map((workspace, index) => (
          <WorkspaceButton key={`workspace-menu-${workspace.workspaceId}`} workspace={workspace} index={index} />
        ))}

        <Button className={styles.newWorkspaceButton} variant={ButtonVariants.filled} onClick={onNewWorkspaceButtonClick}>
          <div>
            <HiPlus className={styles.addIcon} size={10} />
          </div>
        </Button>
      </div>
    </div>
  );
};

export default WorkspaceMenu;
