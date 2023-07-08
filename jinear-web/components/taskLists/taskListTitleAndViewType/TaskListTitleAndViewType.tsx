import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { IoList, IoStatsChart } from "react-icons/io5";
import styles from "./TaskListTitleAndViewType.module.css";

export type TaskDisplayFormat = "LIST" | "WFS_COLUMN";

interface TaskListTitleAndViewTypeProps {
  title: string;
  taskDisplayFormat?: TaskDisplayFormat;
  onTaskDisplayFormatChange?: (format: TaskDisplayFormat) => void;
}

const TaskListTitleAndViewType: React.FC<TaskListTitleAndViewTypeProps> = ({
  title,
  taskDisplayFormat = "LIST",
  onTaskDisplayFormatChange,
}) => {
  const { t } = useTranslation();
  const [displayFormat, setDisplayFormat] = useState<TaskDisplayFormat>(taskDisplayFormat);

  useEffect(() => {
    if (taskDisplayFormat != displayFormat) {
      onTaskDisplayFormatChange?.(displayFormat);
    }
  }, [displayFormat]);

  const changeDisplayFormatToList = () => {
    setDisplayFormat("LIST");
  };

  const changeDisplayFormatToWfsColumn = () => {
    setDisplayFormat("WFS_COLUMN");
  };

  return (
    <div className={styles.container}>
      <div>{title && <h2>{title}</h2>}</div>

      <div className={styles.viewTypeButtonContainer}>
        <Button
          onClick={changeDisplayFormatToList}
          variant={displayFormat == "LIST" ? ButtonVariants.filled2 : ButtonVariants.filled}
          className={styles.button}
          // data-tooltip-right={t("taskListTitleAndViewTypeListTooltip")}
        >
          <IoList />
          {t("taskListTitleAndViewTypeListTooltip")}
        </Button>
        <Button
          onClick={changeDisplayFormatToWfsColumn}
          variant={displayFormat == "WFS_COLUMN" ? ButtonVariants.filled2 : ButtonVariants.filled}
          className={styles.button}
          // data-tooltip-right={t("taskListTitleAndViewTypeStatusColumnsTooltip")}
        >
          <IoStatsChart className={styles.wfsColumnIcon} />
          {t("taskListTitleAndViewTypeStatusColumnsTooltip")}
        </Button>
      </div>
    </div>
  );
};

export default TaskListTitleAndViewType;
