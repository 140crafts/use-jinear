import { WorkspaceDto } from "@/model/be/jinear-core";
import { popWorkspaceSwitchModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import React from "react";
import { LuChevronDown } from "react-icons/lu";
import Button from "../button";
import styles from "./WorkspaceChangeButton.module.css";
interface WorkspaceChangeButtonProps {
  currentWorkspace: WorkspaceDto;
}

const WorkspaceChangeButton: React.FC<WorkspaceChangeButtonProps> = ({ currentWorkspace }) => {
  const dispatch = useAppDispatch();

  const popWorkspacePicker = () => {
    dispatch(popWorkspaceSwitchModal());
  };
  return (
    <Button className={styles.button} onClick={popWorkspacePicker}>
      <div className={cn(styles.label, "single-line")}>{currentWorkspace.title}</div>
      <LuChevronDown size={14} />
    </Button>
  );
};

export default WorkspaceChangeButton;
