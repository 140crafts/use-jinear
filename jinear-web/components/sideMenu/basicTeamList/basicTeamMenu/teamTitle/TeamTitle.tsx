import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, TeamMemberRoleType, WorkspaceDto } from "@/model/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal, IoPeopleOutline } from "react-icons/io5";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  role: TeamMemberRoleType;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ workspace, team, role }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <b className={cn(styles.teamName, "line-clamp")}>{shortenStringIfMoreThanMaxLength({ text: team.name, maxLength: 29 })}</b>
      <Button
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace?.username}/tasks/${team?.username}/members`}
        data-tooltip-right={t("sideMenuTeamMembers")}
      >
        <IoPeopleOutline />
      </Button>
      {role == "ADMIN" && (
        <Button
          variant={ButtonVariants.hoverFilled2}
          href={`/${workspace?.username}/tasks/${team?.username}/settings`}
          data-tooltip-right={t("sideMenuTeamSettings")}
        >
          <IoEllipsisHorizontal />
        </Button>
      )}
    </div>
  );
};

export default TeamTitle;
