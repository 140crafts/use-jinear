import Button, { ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCheckmarkCircleOutline, IoFileTrayOutline, IoPlayForwardOutline } from "react-icons/io5";
import { TiPlus } from "react-icons/ti";
import styles from "./ActionButtonContainer.module.css";

interface ActionButtonContainerProps {}

const ActionButtonContainer: React.FC<ActionButtonContainerProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const _popNewTaskModal = () => {
    dispatch(popNewTaskModal());
  };

  return (
    <div className={styles.container}>
      {/* <MenuGroupTitle label={t("sideMenuActionsTeams")} /> */}
      <Button
        variant={ButtonVariants.hoverFilled2}
        className={cn(styles.button, styles.newTaskButton)}
        onClick={_popNewTaskModal}
      >
        <TiPlus />
        <div>{t("sideMenuNewTask")}</div>
      </Button>

      {!preferredWorkspace?.isPersonal && (
        <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
          <IoFileTrayOutline />
          <div>{t("sideMenuInbox")}</div>
        </Button>
      )}
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoPlayForwardOutline />
        <div>{t("sideMenuActivities")}</div>
      </Button>
      {!preferredWorkspace?.isPersonal && (
        <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
          <IoCheckmarkCircleOutline size={17} style={{ marginLeft: -2 }} />
          <div>{t("sideMenuMyAssignments")}</div>
        </Button>
      )}
    </div>
  );
};

export default ActionButtonContainer;
