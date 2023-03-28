import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { PageDto, TeamMemberDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd } from "react-icons/io5";
import MemberProfilePictureList from "../memberProfilePictureList/MemberProfilePictureList";
import styles from "./WorkspaceTeamMemberList.module.css";

interface WorkspaceTeamMemberListProps {
  page: PageDto<TeamMemberDto>;
}

const WorkspaceTeamMemberList: React.FC<WorkspaceTeamMemberListProps> = ({ page }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <MemberProfilePictureList accountList={page.content.map((member) => member.account)} type="team" />
      <Button
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
        data-tooltip-right={t("sideMenuWorkspaceInviteMember")}
      >
        <IoAdd />
      </Button>
    </div>
  );
};

export default WorkspaceTeamMemberList;
