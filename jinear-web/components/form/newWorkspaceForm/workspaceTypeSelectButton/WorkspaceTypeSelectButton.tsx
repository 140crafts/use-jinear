import Button from "@/components/button";
import cn from "classnames";
import strings from "locales/strings";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPeople, IoPerson, IoSearch } from "react-icons/io5";
import styles from "./WorkspaceTypeSelectButton.module.css";

export type ButtonType = "personal" | "collaborative" | "search";

interface WorkspaceTypeSelectButtonProps {
  onClick: () => void;
  buttonType: ButtonType;
}
const ICON_SIZE = 17;

const IconMap = {
  personal: IoPerson,
  collaborative: IoPeople,
  search: IoSearch,
};
interface IText {
  [key: string]: keyof typeof strings;
}

const TitleMap: IText = {
  personal: "newWorkspaceFormPersonalTitle",
  collaborative: "newWorkspaceFormCollaborativeTitle",
  search: "newWorkspaceFormSearchTitle",
};

const TextMap: IText = {
  personal: "newWorkspaceFormPersonalText",
  collaborative: "newWorkspaceFormCollaborativeText",
  search: "newWorkspaceFormSearchText",
};

const WorkspaceTypeSelectButton: React.FC<WorkspaceTypeSelectButtonProps> = ({ onClick, buttonType }) => {
  const { t } = useTranslation();
  const Icon = IconMap[buttonType];
  return (
    <Button
      disabled={buttonType == "search"}
      onClick={onClick}
      className={cn(styles.container)}
      data-tooltip-right={buttonType == "search" ? t("notYetAvailable") : undefined}
    >
      <div className={cn(styles.header, styles[`header-${buttonType}`])}>
        {<Icon size={ICON_SIZE} />}
        <div className={styles.headerText}>{t(TitleMap[buttonType])}</div>
      </div>
      <div className={cn(styles.infoText, styles[`info-${buttonType}`])}>{t(TextMap[buttonType])}</div>
    </Button>
  );
};

export default WorkspaceTypeSelectButton;
