import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popAddMemberToTeamModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamMembersScreenHeader.module.css";

interface TeamMembersScreenHeaderProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TeamMembersScreenHeader: React.FC<TeamMembersScreenHeaderProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popInviteModal = () => {
    dispatch(popAddMemberToTeamModal({ visible: true, workspace, team }));
  };

  return (
    <div className={styles.container}>
      <SectionTitle title={t("teamMemberScreenListTitle")} description={t("teamMemberScreenListText")} />
      <div className={styles.actionBar}>
        <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={popInviteModal}>
          {t("teamMemberScreenAddMember")}
        </Button>
      </div>
    </div>
  );
};

export default TeamMembersScreenHeader;
