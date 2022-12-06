import { popMenu } from "@/store/slice/displayPreferenceSlice";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoMenu } from "react-icons/io5";
import Button from "../button";
import styles from "./TabBar.module.css";

interface TabBarProps {}

const TabBar: React.FC<TabBarProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const _popMenu = () => {
    dispatch(popMenu());
  };

  const newTask = () => {
    dispatch(popNewTaskModal());
  };

  return (
    <div className={styles.container}>
      <Button onClick={_popMenu} className={styles.button}>
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
