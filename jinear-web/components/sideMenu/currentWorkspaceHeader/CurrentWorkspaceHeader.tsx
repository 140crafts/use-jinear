import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./CurrentWorkspaceHeader.module.scss";

interface CurrentWorkspaceHeaderProps {}

const CurrentWorkspaceHeader: React.FC<CurrentWorkspaceHeaderProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const isTitleSingleWord = preferredWorkspace?.title?.split?.(" ")?.length != 0;
  const isPersonal = preferredWorkspace?.isPersonal;

  return (
    <div className={styles.container}>
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
        className={cn(styles.title, isTitleSingleWord && !isPersonal ? "line-clamp" : undefined)}
        // data-tooltip-right={t("currentWorkspaceHeaderWorkspaceDetail")}
        href={`/${preferredWorkspace?.username}/settings`}
      >
        {isPersonal ? t("workspaceMenuPersonalTitle") : preferredWorkspace?.title}
      </Button>
      {preferredWorkspace && (
        <WorkspaceUpgradeButton workspace={preferredWorkspace} variant={"ICON"} className={styles.upgradeButton} />
      )}
    </div>
  );
};

export default CurrentWorkspaceHeader;
