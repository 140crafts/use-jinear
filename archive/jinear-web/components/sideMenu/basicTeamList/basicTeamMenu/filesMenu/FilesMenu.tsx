import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoDocumentOutline } from "react-icons/io5";
import styles from "./FilesMenu.module.css";

interface FilesMenuProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const FilesMenu: React.FC<FilesMenuProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <Button
        className={styles.labelButton}
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace.username}/tasks/${team.username}/files`}
      >
        <IoDocumentOutline />
        <div>{t("sideMenuTeamActionButtonLabelFiles")}</div>
      </Button>
    </div>
  );
};

export default FilesMenu;
