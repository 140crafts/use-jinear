import Button, { ButtonVariants } from "@/components/button";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { IoCalendarNumberOutline, IoCheckmarkCircleOutline, IoPlayForwardOutline } from "react-icons/io5";
import { TiPlus } from "react-icons/ti";
import styles from "./ActionButtonContainer.module.css";
import InboxButton from "./inboxButton/InboxButton";

interface ActionButtonContainerProps {}

const logger = Logger("ActionButtonContainer");

const ActionButtonContainer: React.FC<ActionButtonContainerProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const router = useRouter();
  const currentPath = router.asPath;
  const calendarPath = `/${preferredWorkspace?.username}`;
  const inboxPath = `/${preferredWorkspace?.username}/inbox`;
  const assignedToMePath = `/${preferredWorkspace?.username}/assigned-to-me`;

  const _popNewTaskModal = () => {
    dispatch(popNewTaskModal());
  };

  logger.log({ preferredTeam });
  return (
    <div className={styles.container}>
      {/* <MenuGroupTitle label={t("sideMenuActionsTeams")} /> */}
      <Button
        variant={ButtonVariants.hoverFilled2}
        className={cn(styles.button, styles.newTaskButton)}
        onClick={_popNewTaskModal}
      >
        <TiPlus size={17} className={styles.addIcon} />
        <div>{t("sideMenuNewTask")}</div>
      </Button>

      <Button
        href={`/${preferredWorkspace?.username}`}
        variant={currentPath == calendarPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoCalendarNumberOutline />
        <div>{t("sideMenuCalendar")}</div>
      </Button>

      <InboxButton isActive={inboxPath == currentPath} workspace={preferredWorkspace} buttonStyle={styles.button} />

      <Button
        href={`/${preferredWorkspace?.username}/${preferredTeam?.username}/last-activities`}
        variant={ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoPlayForwardOutline />
        <div>{t("sideMenuActivities")}</div>
      </Button>

      {!preferredWorkspace?.isPersonal && (
        <Button
          href={assignedToMePath}
          variant={currentPath == assignedToMePath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
          className={styles.button}
        >
          <IoCheckmarkCircleOutline size={17} style={{ marginLeft: -2 }} />
          <div>{t("sideMenuMyAssignments")}</div>
        </Button>
      )}
    </div>
  );
};

export default ActionButtonContainer;
