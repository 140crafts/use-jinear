import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { PageDto, TeamMemberDto } from "@/model/be/jinear-core";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal } from "react-icons/io5";
import MemberProfilePictureList from "../memberProfilePictureList/MemberProfilePictureList";
import styles from "./WorkspaceTeamMemberList.module.css";

interface WorkspaceTeamMemberListProps {
  page: PageDto<TeamMemberDto>;
}

const WorkspaceTeamMemberList: React.FC<WorkspaceTeamMemberListProps> = ({ page }) => {
  const { t } = useTranslation();

  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <MemberProfilePictureList accountList={page.content.map((member) => member.account)} type="team" />
      <Button
        href={`/${workspace?.username}/${team?.username}/members`}
        variant={ButtonVariants.hoverFilled2}
        heightVariant={ButtonHeight.short}
        data-tooltip-right={t("sideMenuTeamMembers")}
      >
        <IoEllipsisHorizontal />
      </Button>
    </div>
  );
};

export default WorkspaceTeamMemberList;
