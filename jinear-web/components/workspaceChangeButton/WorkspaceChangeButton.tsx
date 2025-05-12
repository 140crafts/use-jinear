import { WorkspaceDto } from "@/model/be/jinear-core";
import { popWorkspaceSwitchModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import React from "react";
import { LuChevronDown } from "react-icons/lu";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import styles from "./WorkspaceChangeButton.module.scss";

interface WorkspaceChangeButtonProps {
  currentWorkspace: WorkspaceDto;
}

const WorkspaceChangeButton: React.FC<WorkspaceChangeButtonProps> = ({ currentWorkspace }) => {
  const dispatch = useAppDispatch();

  const popWorkspacePicker = () => {
    dispatch(popWorkspaceSwitchModal());
  };
  return (
    <Button
      className={styles.button}
      variant={ButtonVariants.filled2}
      heightVariant={ButtonHeight.short}
      onClick={popWorkspacePicker}
    >
      <div className={cn(styles.label, "single-line")}>{currentWorkspace.title}</div>
      <div className={styles.iconContainer}>
        <LuChevronDown size={14} className={"icon"} />
      </div>
    </Button>
  );
};

export default WorkspaceChangeButton;
