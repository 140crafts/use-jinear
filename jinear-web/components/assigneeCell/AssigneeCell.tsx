import { TaskDto } from "@/model/be/jinear-core";
import { popChangeTaskAssigneeModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPerson } from "react-icons/io5";
import Button, { ButtonVariants } from "../button";
import styles from "./AssigneeCell.module.css";
import CurrentAccountInfo from "./currentAccountInfo/CurrentAccountInfo";

interface AssigneeCellProps {
  task: TaskDto;
  tooltipPosition?: "left" | "right";
}

const AssigneeCell: React.FC<AssigneeCellProps> = ({
  task,
  tooltipPosition = "right",
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const tooltip = task.assignedToAccount
    ? t("taskWeekCardTaskAssignedToTooltip")?.replace(
        "${to}",
        task?.assignedToAccount?.username || ""
      )
    : t("taskWeekCardTaskHasNoAssignedToTooltip");

  const popChangeAssigneeModal = () => {
    dispatch(popChangeTaskAssigneeModal({ visible: true, task }));
  };

  return task ? (
    <Button
      variant={ButtonVariants.filled}
      className={styles.container}
      data-tooltip-right={tooltipPosition == "right" ? tooltip : undefined}
      data-tooltip={tooltipPosition == "left" ? tooltip : undefined}
      onClick={popChangeAssigneeModal}
    >
      {task.assignedToAccount ? (
        <CurrentAccountInfo assignedToAccount={task.assignedToAccount} />
      ) : (
        <div className={styles.iconContainer}>
          <IoPerson size={12} className={styles.noAssigneeIcon} />
        </div>
      )}
    </Button>
  ) : null;
};

export default AssigneeCell;
