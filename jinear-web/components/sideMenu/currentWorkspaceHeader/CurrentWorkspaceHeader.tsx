import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal } from "react-icons/io5";
import styles from "./CurrentWorkspaceHeader.module.scss";

interface CurrentWorkspaceHeaderProps {}

const CurrentWorkspaceHeader: React.FC<CurrentWorkspaceHeaderProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const isTitleSingleWord = preferredWorkspace?.title?.split?.(" ")?.length != 0;
  const isPersonal = preferredWorkspace?.isPersonal;

  return (
    <div className={styles.container}>
      <div className={cn(styles.title, isTitleSingleWord && !isPersonal ? "single-line" : undefined)}>
        {isPersonal ? t("workspaceMenuPersonalTitle") : preferredWorkspace?.title}
      </div>
      <div className="flex-1" />
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
        data-tooltip-right={t("currentWorkspaceHeaderWorkspaceDetail")}
        className={styles.button}
      >
        <IoEllipsisHorizontal size={17} />
      </Button>
    </div>
  );
};

export default CurrentWorkspaceHeader;
