import React from "react";
import styles from "./MilestoneTaskList.module.scss";
import { MilestoneDto } from "@/be/jinear-core";
import PrefilteredPaginatedTaskList
  from "@/components/taskLists/prefilteredPaginatedTaskList/PrefilteredPaginatedTaskList";
import MilestoneDescription
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneTaskList/milestoneDescription/MilestoneDescription";
import MilestoneTitle
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneTaskList/milestoneTitle/MilestoneTitle";
import MilestoneTargetDate
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneTaskList/milestoneTargetDate/MilestoneTargetDate";
import useTranslation from "@/locals/useTranslation";
import { LuDiamond, LuTrash, LuX } from "react-icons/lu";
import cn from "classnames";
import Button, { ButtonVariants } from "@/components/button";
import MilestoneDeleteButton
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneTaskList/milestoneDeleteButton/MilestoneDeleteButton";

interface MilestoneTaskListProps {
  workspaceId: string;
  milestone: MilestoneDto;
  isFirst: boolean;
  isLast: boolean;
}

const MilestoneTaskList: React.FC<MilestoneTaskListProps> = ({ isFirst, isLast, workspaceId, milestone }) => {
  const { t } = useTranslation();

  return (
    <div className={cn(styles.container, isLast && styles.lastContainer)}>

      <div className={styles.iconContainer}>
        <LuDiamond className={cn("icon", styles.icon, isFirst && styles.firstIcon)} />
        {!isLast && <div className={styles.line} />}
      </div>

      <div className={styles.contentContainer}>
        <div className={styles.milestoneInfoContainer}>
          <div className={styles.milestoneTitleContainer}>
            <MilestoneTitle milestoneId={milestone.milestoneId} title={milestone.title} />
            <MilestoneTargetDate milestoneId={milestone.milestoneId} targetDate={milestone.targetDate} />
            <MilestoneDeleteButton milestoneId={milestone.milestoneId} title={milestone.title} />
          </div>
          <MilestoneDescription milestoneId={milestone.milestoneId} description={milestone.description} />
        </div>

        <div>
          <h3>{t("milestoneTasks")}</h3>
          <PrefilteredPaginatedTaskList
            id={`project-milestone-task-list-${milestone.milestoneId}`}
            filter={{
              workspaceId: workspaceId,
              milestoneIds: [milestone.milestoneId]
            }}
          />
        </div>

      </div>
    </div>
  );
};

export default MilestoneTaskList;