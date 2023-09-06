import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal, IoPeopleOutline } from "react-icons/io5";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <b className={cn(styles.teamName, "line-clamp")}>{shortenStringIfMoreThanMaxLength({ text: team.name, maxLength: 29 })}</b>
      <Button
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace?.username}/${team?.username}/members`}
        data-tooltip-right={t("sideMenuTeamMembers")}
      >
        <IoPeopleOutline />
      </Button>
      <Button
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace?.username}/${team?.username}/settings`}
        data-tooltip-right={t("sideMenuTeamSettings")}
      >
        <IoEllipsisHorizontal />
      </Button>
    </div>
  );
};

export default TeamTitle;
