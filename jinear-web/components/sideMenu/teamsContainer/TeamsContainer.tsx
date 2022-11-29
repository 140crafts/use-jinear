import { ButtonVariants } from "@/components/button";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import MenuGroupTitle from "../menuGroupTitle/MenuGroupTitle";
import TeamMenu from "./teamMenu/TeamMenu";
import styles from "./TeamsContainer.module.css";

interface TeamsContainerProps {}

const TeamsContainer: React.FC<TeamsContainerProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );

  const {
    data: teamsResponse,
    isSuccess,
    isError,
    isLoading,
  } = useRetrieveWorkspaceTeamsQuery(preferredWorkspace?.workspaceId || "", {
    skip: preferredWorkspace?.workspaceId == null,
  });

  return (
    <div className={styles.container}>
      <div className={styles.teamsTitleContainer}>
        <MenuGroupTitle
          label={t("sideMenuWorkspaceTeams")}
          hasAddButton={true}
          buttonVariant={
            teamsResponse?.data.length == 0
              ? ButtonVariants.filled2
              : ButtonVariants.hoverFilled2
          }
        />
      </div>
      <div className={styles.teamsList}>
        {isLoading && <CircularProgress size={15} className={styles.loading} />}
        {isSuccess && teamsResponse.data.length == 0 && (
          <div className={styles.noTeamLabel}>
            {t("sideMenuTeamListNoTeam")}
          </div>
        )}
        {isSuccess &&
          teamsResponse?.data?.map((team) => (
            <TeamMenu key={`menu-${team.teamId}`} {...team} />
          ))}
      </div>
    </div>
  );
};

export default TeamsContainer;
