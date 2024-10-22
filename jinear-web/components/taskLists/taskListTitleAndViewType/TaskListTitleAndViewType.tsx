import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { IoList, IoStatsChart } from "react-icons/io5";
import styles from "./TaskListTitleAndViewType.module.css";

export type TaskDisplayFormat = "LIST" | "WFS_COLUMN";

interface TaskListTitleAndViewTypeProps {
  title: string;
  teamUsername: string;
  taskDisplayFormat?: TaskDisplayFormat;
  onTaskDisplayFormatChange?: (format: TaskDisplayFormat) => void;
}

const setTeamDefaultDisplayFormat = (teamUsername: string, format: TaskDisplayFormat) => {
  if (typeof window === "object") {
    localStorage.setItem(`df-${teamUsername}`, format);
  }
};

const TaskListTitleAndViewType: React.FC<TaskListTitleAndViewTypeProps> = ({
                                                                             title,
                                                                             teamUsername,
                                                                             taskDisplayFormat = "WFS_COLUMN",
                                                                             onTaskDisplayFormatChange
                                                                           }) => {
  const { t } = useTranslation();
  const [displayFormat, setDisplayFormat] = useState<TaskDisplayFormat>(taskDisplayFormat);

  useEffect(() => {
    if (taskDisplayFormat != displayFormat) {
      onTaskDisplayFormatChange?.(displayFormat);
    }
  }, [displayFormat]);

  useEffect(() => {
    setDisplayFormat(taskDisplayFormat);
  }, [taskDisplayFormat]);

  const changeDisplayFormatToList = () => {
    setDisplayFormat("LIST");
    setTeamDefaultDisplayFormat(teamUsername, "LIST");
  };

  const changeDisplayFormatToWfsColumn = () => {
    setDisplayFormat("WFS_COLUMN");
    setTeamDefaultDisplayFormat(teamUsername, "WFS_COLUMN");
  };

  return (
    <div className={styles.container}>
      <div>{title && <h2>{title}</h2>}</div>

      <div className={styles.viewTypeButtonContainer}>
        <Button
          onClick={changeDisplayFormatToList}
          variant={displayFormat == "LIST" ? ButtonVariants.filled2 : ButtonVariants.filled}
          className={styles.button}
          data-tooltip-right={t("taskListTitleAndViewTypeListTooltip")}
          heightVariant={ButtonHeight.short}
        >
          <IoList />
          {t("taskListTitleAndViewTypeListLabel")}
        </Button>
        <Button
          onClick={changeDisplayFormatToWfsColumn}
          variant={displayFormat == "WFS_COLUMN" ? ButtonVariants.filled2 : ButtonVariants.filled}
          className={styles.button}
          data-tooltip-right={t("taskListTitleAndViewTypeStatusColumnsTooltip")}
          heightVariant={ButtonHeight.short}
        >
          <IoStatsChart className={styles.wfsColumnIcon} />
          {t("taskListTitleAndViewTypeStatusColumnsLabel")}
        </Button>
      </div>
    </div>
  );
};

export default TaskListTitleAndViewType;
