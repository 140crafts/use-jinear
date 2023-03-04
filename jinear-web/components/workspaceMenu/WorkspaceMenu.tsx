import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { popNewWorkspaceModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { HiPlus } from "react-icons/hi";
import Button, { ButtonVariants } from "../button";
import WorkspaceButton from "./workspaceButton/WorkspaceButton";
import styles from "./WorkspaceMenu.module.css";

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
      {workspaces?.map((workspace) => (
        <WorkspaceButton key={`workspace-menu-${workspace.workspaceId}`} workspace={workspace} />
      ))}
      <Button
        className={styles.button}
        variant={ButtonVariants.hoverFilled2}
        data-tooltip={t("newWorkspaceButtonTooltip")}
        onClick={onNewWorkspaceButtonClick}
      >
        <HiPlus size={17} />
      </Button>
    </div>
  );
};

export default WorkspaceMenu;
