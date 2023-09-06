import { WorkspaceDto } from "@/model/be/jinear-core";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto } from "@/store/slice/accountSlice";
import { popUpgradeWorkspacePlanModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { isWorkspaceUpgradable } from "@/utils/permissionHelper";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoSparkles } from "react-icons/io5";
import Button, { ButtonHeight } from "../button";
import styles from "./WorkspaceUpgradeButton.module.css";

interface WorkspaceUpgradeButtonProps {
  workspace: WorkspaceDto;
  variant: "ICON" | "FULL";
  className?: string;
}

const logger = Logger("WorkspaceUpgradeButton");

const WorkspaceUpgradeButton: React.FC<WorkspaceUpgradeButtonProps> = ({ workspace, variant, className }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  logger.log({ workspace });

  const workspaceRoleIsAdminOrOwner = useTypedSelector(
    selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto(workspace)
  );

  const popUpgradeModal = () => {
    dispatch(popUpgradeWorkspacePlanModal({ workspaceId: workspace.workspaceId, visible: true }));
  };

  return isWorkspaceUpgradable(workspace) && workspaceRoleIsAdminOrOwner ? (
    <Button
      heightVariant={ButtonHeight.short}
      className={cn(styles.container, className)}
      data-tooltip-right={variant == "ICON" ? t("workspaceTierUpgradeButton") : undefined}
      onClick={popUpgradeModal}
    >
      <IoSparkles />
      {variant == "FULL" && (
        <>
          <div className="spacer-w-1" />
          {t("workspaceTierUpgradeButton")}
        </>
      )}
    </Button>
  ) : null;
};

export default WorkspaceUpgradeButton;
