import useWindowSize from "@/hooks/useWindowSize";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { closeTeamPickerModal, selectTeamPickerModalVisible, selectTeamPickerModalWorkspaceId } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./TeamPickerModal.module.css";
import BasicTeamButton from "./basicTeamButton/BasicTeamButton";

interface TeamPickerModalProps {}

const TeamPickerModal: React.FC<TeamPickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectTeamPickerModalVisible);
  const workspaceId = useTypedSelector(selectTeamPickerModalWorkspaceId) || "";

  const { data: teamsResponse, isSuccess, isError, isLoading, isFetching } = useRetrieveWorkspaceTeamsQuery(workspaceId);

  const close = () => {
    dispatch(closeTeamPickerModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("teamSelectMenu")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {isFetching && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={17} />
        </div>
      )}
      {teamsResponse && <div className={styles.title}>{t("teamPickerModalPickATeam")}</div>}
      {teamsResponse?.data.map((teamDto) => (
        <BasicTeamButton key={`basic-team-button-${teamDto.teamId}`} team={teamDto} close={close} />
      ))}
    </Modal>
  );
};

export default TeamPickerModal;
