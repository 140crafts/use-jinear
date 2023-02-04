import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { useUpdatePreferredTeamMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import {
  changeLoadingModalVisibility,
  popNewTeamModal,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { IoAdd, IoEllipsisHorizontal, IoScan } from "react-icons/io5";
import MenuGroupTitle from "../menuGroupTitle/MenuGroupTitle";
import TeamMenu from "./teamMenu/TeamMenu";
import styles from "./TeamsContainer.module.css";

interface TeamsContainerProps {}

const logger = Logger("TeamsContainer");

const TeamsContainer: React.FC<TeamsContainerProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const router = useRouter();

  const preferredWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeamId = useTypedSelector(
    selectCurrentAccountsPreferredTeamId
  );

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
    skip: preferredWorkspace?.workspaceId == null,
  });

  const preferredTeam = teamsResponse?.data?.find(
    (team) => team.teamId == preferredTeamId
  );
  const selectedTeam = preferredTeam || teamsResponse?.data?.[0];

  useEffect(() => {
    const isChanged = selectedTeam?.teamId != preferredTeam?.teamId;
    const isInSameWorkspace =
      selectedTeam?.workspaceId == preferredTeam?.workspaceId;
    logger.log({ isSelectedTeamChanged: isChanged });
    if (isChanged && selectedTeam && isInSameWorkspace) {
      updatePreferredTeamMutation({
        workspaceId: selectedTeam.workspaceId,
        teamId: selectedTeam.teamId,
      });
    }
  }, [selectedTeam, preferredTeam]);

  useEffect(() => {
    dispatch(
      changeLoadingModalVisibility({ visible: isUpdatePreferredTeamLoading })
    );
  }, [isUpdatePreferredTeamLoading]);

  const routeTeamSettings = () => {
    if (selectedTeam) {
      router.push(
        `/${preferredWorkspace?.username}/${selectedTeam.name}/settings`
      );
    }
  };

  const routeTeamHome = () => {
    if (selectedTeam) {
      router.push(`/${preferredWorkspace?.username}/${selectedTeam.name}`);
    }
  };

  const _popNewTeamModal = () => {
    dispatch(popNewTeamModal());
  };

  return (
    <div className={styles.container}>
      {!preferredWorkspace?.isPersonal && (
        <div className={styles.teamsTitleContainer}>
          <MenuGroupTitle
            label={t("sideMenuWorkspaceCurrentTeam")}
            buttonVariant={
              teamsResponse?.data.length == 0
                ? ButtonVariants.filled2
                : ButtonVariants.hoverFilled2
            }
          />
          <Button
            variant={ButtonVariants.hoverFilled2}
            onClick={routeTeamHome}
            heightVariant={ButtonHeight.short}
          >
            <IoScan />
          </Button>
          <Button
            variant={ButtonVariants.hoverFilled2}
            onClick={routeTeamSettings}
            heightVariant={ButtonHeight.short}
          >
            <IoEllipsisHorizontal />
          </Button>
          <Button
            variant={ButtonVariants.hoverFilled2}
            onClick={_popNewTeamModal}
            heightVariant={ButtonHeight.short}
          >
            <IoAdd />
          </Button>
        </div>
      )}
      <div className={styles.teamsList}>
        {isLoading && <CircularProgress size={15} className={styles.loading} />}
        {isSuccess && teamsResponse.data.length == 0 && (
          <div className={styles.noTeamLabel}>
            {t("sideMenuTeamListNoTeam")}
          </div>
        )}
        {isSuccess && selectedTeam && (
          <TeamMenu
            {...selectedTeam}
            isPersonalWorkspace={preferredWorkspace?.isPersonal}
            workspaceUsername={preferredWorkspace?.username || ""}
          />
        )}
      </div>
    </div>
  );
};

export default TeamsContainer;
