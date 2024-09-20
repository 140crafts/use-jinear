import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import useWindowSize from "@/hooks/useWindowSize";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { closeMenuMoreActionModal, selectMenuMoreActionModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./MenuMoreActionModal.module.css";
import WorkspaceMembersButton from "./workspaceMembersButton/WorkspaceMembersButton";
import WorkspaceSettingsButton from "./workspaceSettingsButton/WorkspaceSettingsButton";
import InstallPwaAppButton from "@/components/installPwaAppButton/InstallPwaAppButton";
import isPwa from "@/utils/pwaHelper";

interface MenuMoreActionModalProps {
}

const MenuMoreActionModal: React.FC<MenuMoreActionModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectMenuMoreActionModalVisible);
  const { isMobile } = useWindowSize();
  const _isPwa = isPwa();

  const params = useParams();
  const workspaceName = params?.workspaceName as string | undefined;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName || ""));

  const close = () => {
    dispatch(closeMenuMoreActionModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("moreMenuActionModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.contentContainer}
    >
      <div className={styles.buttonsContainer}>
        {workspace && <WorkspaceUpgradeButton workspace={workspace} variant={"FULL"} className={styles.upgradeButton} />}
        {!_isPwa && <InstallPwaAppButton className={styles.pwiButton} withLabel={true} />}
      </div>
      <WorkspaceSettingsButton workspaceName={workspaceName} requestClose={close} />
      <WorkspaceMembersButton workspaceName={workspaceName} requestClose={close} />
    </Modal>
  );
};

export default MenuMoreActionModal;
