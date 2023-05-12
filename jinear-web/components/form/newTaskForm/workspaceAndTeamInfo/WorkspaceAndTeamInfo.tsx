import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamOptionsModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoChevronForward } from "react-icons/io5";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popChangeTeamModal = () => {
    dispatch(popTeamOptionsModal());
  };

  return (
    <div className={styles.container}>
      {t("newTaskFormWorkspaceAndTeamInfoLabel")}
      <div className={styles.buttonContainer}>
        <Button disabled heightVariant={ButtonHeight.short}>
          <b>{workspace.isPersonal ? t("newTaskFormPersonalWorkspaceSelected") : workspace.title}</b>
        </Button>
        {!workspace.isPersonal && (
          <>
            <IoChevronForward size={15} />
            <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popChangeTeamModal}>
              <b>{team.name}</b>
            </Button>
          </>
        )}
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
