import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import LastTeamActivitiesList from "../lastActivitiesScreen/lastTeamActivitiesList/LastTeamActivitiesList";
import styles from "./TeamLastActivities.module.css";

interface TeamLastActivitiesProps {
  workspaceId: string;
  teamId: string;
  className?: string;
}

const TeamLastActivities: React.FC<TeamLastActivitiesProps> = ({ workspaceId, teamId, className }) => {
  const { t } = useTranslation();
  return (
    <div className={cn(styles.container, className)}>
      <span>{t("teamLastActivitiesTitle")}</span>
      <LastTeamActivitiesList
        workspaceId={workspaceId}
        teamId={teamId}
        contentContainerClassName={styles.listContentContainerClassName}
        size={10}
      />
    </div>
  );
};

export default TeamLastActivities;
