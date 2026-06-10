import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { TeamDto, TeamStateType } from "@/model/be/jinear-core";
import { useUpdateTeamStateMutation } from "@/store/api/teamApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamStateSettings.module.scss";

interface TeamStateSettingsProps {
  team: TeamDto;
}

const TEAM_STATES: TeamStateType[] = ["ARCHIVED", "ACTIVE"];

const TeamStateSettings: React.FC<TeamStateSettingsProps> = ({ team }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentState = team.teamState;

  const [updateTeamState, {}] = useUpdateTeamStateMutation();

  const toggleState = () => {
    const nextState = TEAM_STATES[(TEAM_STATES.indexOf(currentState) + 1) % TEAM_STATES.length];
    updateTeamState({ teamId: team.teamId, teamState: nextState });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModal = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t(`teamSettingsScreenTeamStateSectionTitle_${currentState}`),
        content: t(`teamSettingsScreenTeamStateSectionDescription_${currentState}`),
        confirmButtonLabel: t(`teamSettingsScreenTeamStateSectionActionButton_${currentState}`),
        onConfirm: toggleState,
      })
    );
  };

  return (
    <div className={styles.container}>
      <SectionTitle
        title={t(`teamSettingsScreenTeamStateSectionTitle_${currentState}`)}
        description={t(`teamSettingsScreenTeamStateSectionDescription_${currentState}`)}
      />
      <div className={styles.actionButtonContainer}>
        <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popAreYouSureModal}>
          {t(`teamSettingsScreenTeamStateSectionActionButton_${currentState}`)}
        </Button>
      </div>
    </div>
  );
};

export default TeamStateSettings;
