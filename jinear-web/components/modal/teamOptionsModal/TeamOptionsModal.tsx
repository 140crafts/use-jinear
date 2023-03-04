import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { closeTeamOptionsModal, selectTeamOptionsModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import TeamList from "./teamList/TeamList";
import styles from "./TeamOptionsModal.module.css";

interface TeamOptionsModalProps {}

const TeamOptionsModal: React.FC<TeamOptionsModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTeamOptionsModalVisible);
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);

  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeTeamOptionsModal());
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
      {preferredWorkspace && preferredTeamId && (
        <TeamList preferredWorkspace={preferredWorkspace} preferredTeamId={preferredTeamId} />
      )}
    </Modal>
  );
};

export default TeamOptionsModal;
