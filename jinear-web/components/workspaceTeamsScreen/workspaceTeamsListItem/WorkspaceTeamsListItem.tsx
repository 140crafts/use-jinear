import React from "react";
import styles from "./WorkspaceTeamsListItem.module.css";
import { TeamDto, WorkspaceDto } from "@/be/jinear-core";
import { useCurrentAccountsTeamMembership } from "@/hooks/useCurrentAccountsTeamMembership";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { useWorkspaceRole } from "@/hooks/useWorkspaceRole";
import { IoEllipsisHorizontal, IoPeople } from "react-icons/io5";
import Logger from "@/utils/logger";
import { useJoinTeamMutation, useLeaveTeamMutation } from "@/api/teamMemberApi";

interface WorkspaceTeamsListItemProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const logger = Logger("WorkspaceTeamsListItem");

const WorkspaceTeamsListItem: React.FC<WorkspaceTeamsListItemProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  const [joinTeam, { isLoading: isJoinLoading }] = useJoinTeamMutation();
  const [leaveTeam, { isLoading: isLeaveLoading }] = useLeaveTeamMutation();

  const teamMembership = useCurrentAccountsTeamMembership({ workspaceId: team.workspaceId, teamId: team.teamId });
  const isTeamMember = !!teamMembership;
  const workspaceRole = useWorkspaceRole({ workspaceId: team.workspaceId }) || "";
  const teamRole = teamMembership?.role;
  const isWorkspaceAdminOrOwner = ["ADMIN", "OWNER"].indexOf(workspaceRole) != -1;
  const isTeamAdmin = teamRole == "ADMIN";

  const join = () => {
    joinTeam({ teamId: team.teamId });
  };

  const leave = () => {
    leaveTeam({ teamId: team.teamId });
  };

  return (
    <div className={styles.container}>
      <div className={styles.infoContainer}>
        <Button
          heightVariant={ButtonHeight.short}
          disabled={isJoinLoading || isLeaveLoading}
          href={`/${workspace.username}/tasks/${team.username}`}
          className={styles.homeButtonIcon}
        >
          <b>{team.name}</b>
          <div className={styles.tag}>{`(${team.tag})`}</div>
        </Button>
      </div>

      <Button
        heightVariant={ButtonHeight.short}
        variant={isTeamMember ? ButtonVariants.default : ButtonVariants.filled}
        onClick={isTeamMember ? leave : join}
        disabled={isJoinLoading || isLeaveLoading}
        loading={isJoinLoading || isLeaveLoading}
      >
        {t(isTeamMember ? "workspaceTeamsListItemLeaveButton" : "workspaceTeamsListItemJoinButton")}
      </Button>

      <Button
        heightVariant={ButtonHeight.short}
        href={`/${workspace.username}/tasks/${team.username}/members`}
        data-tooltip-right={t("sideMenuTeamMembers")}>
        <IoPeople />
      </Button>

      {(isWorkspaceAdminOrOwner || isTeamAdmin) &&
        <Button
          heightVariant={ButtonHeight.short}
          href={`/${workspace.username}/tasks/${team.username}/settings`}
          data-tooltip-right={t("sideMenuTeamSettings")}
        >
          <IoEllipsisHorizontal />
        </Button>
      }

    </div>
  );
};

export default WorkspaceTeamsListItem;