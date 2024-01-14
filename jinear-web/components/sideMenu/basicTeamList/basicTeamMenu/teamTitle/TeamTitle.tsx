import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, TeamMemberRoleType, WorkspaceDto } from "@/model/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
import React from "react";
import { IoEllipsisHorizontal, IoHome, IoPeopleOutline } from "react-icons/io5";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  role: TeamMemberRoleType;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ workspace, team, role }) => {
  const { t } = useTranslation();

  const currentPath = usePathname();
  const homePath = `/${workspace?.username}/tasks/${team?.username}`;
  const membersPath = `/${workspace?.username}/tasks/${team?.username}/members`;
  const settingsPath = `/${workspace?.username}/tasks/${team?.username}/settings`;

  return (
    <div className={styles.container}>
      <b className={cn(styles.teamName, "single-line")}>
        {shortenStringIfMoreThanMaxLength({
          text: team.name,
          maxLength: 29,
        })}
      </b>
      <Button
        variant={homePath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        href={homePath}
        data-tooltip-right={t("sideMenuTeamHome")}
      >
        <IoHome />
      </Button>
      <Button
        variant={membersPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        href={membersPath}
        data-tooltip-right={t("sideMenuTeamMembers")}
      >
        <IoPeopleOutline />
      </Button>
      {role == "ADMIN" && (
        <Button
          variant={settingsPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
          href={settingsPath}
          data-tooltip-right={t("sideMenuTeamSettings")}
        >
          <IoEllipsisHorizontal />
        </Button>
      )}
    </div>
  );
};

export default TeamTitle;
