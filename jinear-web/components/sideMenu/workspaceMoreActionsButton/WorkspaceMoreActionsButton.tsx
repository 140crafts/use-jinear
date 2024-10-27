import Button, { ButtonVariants } from "@/components/button";
import { popMenuMoreActionModal, selectMenuMoreActionModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuSettings2 } from "react-icons/lu";
import { useDispatch } from "react-redux";
import styles from "./WorkspaceMoreActionsButton.module.css";

interface WorkspaceMoreActionsButtonProps {}

const WorkspaceMoreActionsButton: React.FC<WorkspaceMoreActionsButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useDispatch();
  const menuMoreActionModalVisible = useTypedSelector(selectMenuMoreActionModalVisible);

  const popMoreActionMenu = () => {
    dispatch(popMenuMoreActionModal());
  };

  return (
    <Button
      className={styles.iconButton}
      onClick={popMoreActionMenu}
      variant={ButtonVariants.filled2}
      data-tooltip={t("mainFeaturesMenuLabelMore")}
    >
      <LuSettings2 className={styles.icon} />
    </Button>
  );
};

export default WorkspaceMoreActionsButton;
