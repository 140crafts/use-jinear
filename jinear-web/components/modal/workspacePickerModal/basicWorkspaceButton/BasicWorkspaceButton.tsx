import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { selectWorkspacePickerModalCurrentWorkspaceId, selectWorkspacePickerModalOnPick } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./BasicWorkspaceButton.module.css";

interface BasicWorkspaceButtonProps {
  workspace: WorkspaceDto;
  close: () => void;
}

const BasicWorkspaceButton: React.FC<BasicWorkspaceButtonProps> = ({ workspace, close }) => {
  const { t } = useTranslation();
  const onPick = useTypedSelector(selectWorkspacePickerModalOnPick);
  const currentWorkspaceId = useTypedSelector(selectWorkspacePickerModalCurrentWorkspaceId);
  const isSelected = currentWorkspaceId == workspace.workspaceId;

  const onClick = () => {
    onPick?.(workspace);
    close();
  };

  return (
    <Button
      onClick={onClick}
      className={cn(styles.container, isSelected && styles.selectedWorkspace)}
      variant={isSelected ? ButtonVariants.filled : ButtonVariants.default}
    >
      {workspace.title}
    </Button>
  );
};

export default BasicWorkspaceButton;
