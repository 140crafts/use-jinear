import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { toggleMenu } from "@/store/slice/displayPreferenceSlice";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoMenu } from "react-icons/io5";
import Button from "../button";
import styles from "./TabBar.module.css";

interface TabBarProps {}

const TabBar: React.FC<TabBarProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(preferredWorkspace?.workspaceId || "", {
    skip: preferredWorkspace == null,
  });
  const team = teamsResponse?.data?.find((team) => team);

  const _toggleMenu = () => {
    dispatch(toggleMenu());
  };

  const newTask = () => {
    if (preferredWorkspace) {
      dispatch(popNewTaskModal({ visible: true, workspace: preferredWorkspace, team }));
    }
  };

  return (
    <div className={styles.container}>
      <Button onClick={_toggleMenu} className={styles.button}>
        <IoMenu />
        {t("tabBarMenu")}
      </Button>
      <Button onClick={newTask} className={styles.button}>
        <IoAdd />
        {t("sideMenuNewTask")}
      </Button>
    </div>
  );
};

export default TabBar;
