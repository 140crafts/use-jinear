import Button, { ButtonVariants } from "@/components/button";
import { TeamDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import { IoEllipsisHorizontal } from "react-icons/io5";
import styles from "./TeamTitle.module.css";

interface TeamTitleProps {
  team: TeamDto;
}

const TeamTitle: React.FC<TeamTitleProps> = ({ team }) => {
  return (
    <div className={styles.container}>
      <b className={cn(styles.teamName, "line-clamp")}>{team.name}</b>
      <Button variant={ButtonVariants.hoverFilled2}>
        <IoEllipsisHorizontal />
      </Button>
    </div>
  );
};

export default TeamTitle;
