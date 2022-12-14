import ProfilePhoto from "@/components/profilePhoto";
import { TaskDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./AssigneeCell.module.css";

interface AssigneeCellProps {
  task: TaskDto;
  tooltipPosition?: "left" | "right";
}

const AssigneeCell: React.FC<AssigneeCellProps> = ({
  task,
  tooltipPosition = "right",
}) => {
  const { t } = useTranslation();
  const tooltip = t("taskWeekCardTaskAssignedToTooltip")?.replace(
    "${to}",
    task?.assignedToAccount?.username || ""
  );

  return task && task.assignedToAccount ? (
    <div
      className={styles.container}
      data-tooltip-right={tooltipPosition == "right" ? tooltip : undefined}
      data-tooltip={tooltipPosition == "left" ? tooltip : undefined}
    >
      {task.assignedToAccount.profilePicture ? (
        <ProfilePhoto
          boringAvatarKey={task.assignedToAccount.accountId}
          storagePath={task.assignedToAccount.profilePicture?.storagePath}
          wrapperClassName={styles.profilePic}
        />
      ) : (
        <div className={styles.noPicChar}>
          {task.assignedToAccount.username
            .substring(0, 1)
            ?.toLocaleUpperCase?.()}
        </div>
      )}
    </div>
  ) : null;
};

export default AssigneeCell;
