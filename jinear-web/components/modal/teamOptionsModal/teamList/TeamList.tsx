import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { useUpdatePreferredTeamMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamList.module.css";

interface TeamListProps {
  preferredWorkspace: WorkspaceDto;
  preferredTeamId: string;
}

const TeamList: React.FC<TeamListProps> = ({
  preferredWorkspace,
  preferredTeamId,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [
    updatePreferredTeamMutation,
    {
      isSuccess: isUpdatePreferredTeamSuccess,
      isLoading: isUpdatePreferredTeamLoading,
    },
  ] = useUpdatePreferredTeamMutation();

  const {
    data: teamsResponse,
    isSuccess,
    isError,
    isLoading,
  } = useRetrieveWorkspaceTeamsQuery(preferredWorkspace?.workspaceId || "", {
    skip: preferredWorkspace == null,
  });

  const preferredTeam = teamsResponse?.data?.find(
    (team) => team.teamId == preferredTeamId
  );
  const selectedTeam = preferredTeam || teamsResponse?.data?.[0];

  const _changePreferredTeam = (workspaceId: string, teamId: string) => {
    dispatch(changeLoadingModalVisibility({ visible: true }));
    updatePreferredTeamMutation({ workspaceId, teamId });
  };

  return (
    <div className={styles.container}>
      <div className={styles.title}>
        {t("teamOptionsSelectActiveTeamTitle")}
      </div>
      <div className={styles.listContainer}>
        {isLoading && <CircularProgress size={15} className={styles.loading} />}
        {isSuccess && teamsResponse.data.length == 0 && (
          <div className={styles.noTeamLabel}>
            {t("sideMenuTeamListNoTeam")}
          </div>
        )}
        {isSuccess &&
          teamsResponse.data.map((team) => (
            <Button
              key={`change-team-${team.teamId}`}
              className={cn(
                styles.button,
                team.teamId == selectedTeam?.teamId
                  ? styles.currentTeamButton
                  : null
              )}
              variant={
                team.teamId != selectedTeam?.teamId
                  ? ButtonVariants.default
                  : ButtonVariants.filled
              }
              onClick={() => {
                _changePreferredTeam(team.workspaceId, team.teamId);
              }}
            >
              {team.name}
            </Button>
          ))}
      </div>
    </div>
  );
};

export default TeamList;
