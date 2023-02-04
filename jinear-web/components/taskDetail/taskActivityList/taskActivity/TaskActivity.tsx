import Button from "@/components/button";
import Transition from "@/components/transition/Transition";
import { useToggle } from "@/hooks/useToggle";
import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import decideWorkspaceActivityIcon from "@/utils/workspaceActivityIconMap";
import {
  differenceInDays,
  differenceInHours,
  differenceInMinutes,
  format,
} from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import AssignedDateChangeDiffInfo from "./assignedDateChangeDiffInfo/AssignedDateChangeDiffInfo";
import AssigneeChangeDiffInfo from "./assigneeChangeDiffInfo/AssigneeChangeDiffInfo";
import DescDiffInfo from "./descDiffInfo/DescDiffInfo";
import DueDateChangeDiffInfo from "./dueDateChangeDiffInfo/DueDateChangeDiffInfo";
import styles from "./TaskActivity.module.css";
import TaskRelationChangedDiffInfo from "./taskRelationChangedDiffInfo/TaskRelationChangedDiffInfo";
import TitleDiffInfo from "./titleDiffInfo/TitleDiffInfo";
import TopicDiffInfo from "./topicDiffInfo/TopicDiffInfo";
import WorkflowStatusDiffInfo from "./workflowStatusDiffInfo/WorkflowStatusDiffInfo";

interface TaskActivityProps {
  activity: WorkspaceActivityDto;
}
const TASK_RELATED_ACTIONS_WITH_DIFF = [
  "TASK_CLOSED",
  "EDIT_TASK_TITLE",
  "EDIT_TASK_DESC",
  "TASK_UPDATE_TOPIC",
  "TASK_UPDATE_WORKFLOW_STATUS",
  "TASK_CHANGE_ASSIGNEE",
  "TASK_CHANGE_ASSIGNED_DATE",
  "TASK_CHANGE_DUE_DATE",
  "RELATION_INITIALIZED",
  "RELATION_REMOVED",
];

const TaskActivity: React.FC<TaskActivityProps> = ({ activity }) => {
  const { t } = useTranslation();
  const { current: diffVisible, toggle: toggleDiff } = useToggle();

  const Icon = useMemo(
    () => decideWorkspaceActivityIcon(activity.type),
    [activity.workspaceActivityId]
  );

  const dateDiff = useMemo(() => {
    const diffInDays = differenceInDays(
      new Date(),
      new Date(activity.createdDate)
    );
    if (diffInDays != 0) {
      return t("taskWorkflowActivityInfoLabelDateInDays")?.replace(
        "${num}",
        `${diffInDays}`
      );
    }
    const diffInHours = differenceInHours(
      new Date(),
      new Date(activity.createdDate)
    );
    if (diffInHours != 0) {
      return t("taskWorkflowActivityInfoLabelDateInHours")?.replace(
        "${num}",
        `${diffInHours}`
      );
    }
    const diffInMinutes = differenceInMinutes(
      new Date(),
      new Date(activity.createdDate)
    );
    if (diffInMinutes != 0) {
      return t("taskWorkflowActivityInfoLabelDateInMinutes")?.replace(
        "${num}",
        `${diffInMinutes}`
      );
    }
    return t("taskWorkflowActivityInfoLabelDateJustNow");
  }, [activity.createdDate]);

  const createdDate = format(
    new Date(activity.createdDate),
    t("dateTimeFormat")
  );

  return (
    <div className={styles.container}>
      <Button
        onClick={
          TASK_RELATED_ACTIONS_WITH_DIFF.indexOf(activity.type) != -1
            ? toggleDiff
            : undefined
        }
        // variant={ButtonVariants.filled}
        className={styles.contentContainer}
      >
        <div>{<Icon size={15} className={styles.icon} />}</div>
        <div className={styles.labelContainer}>
          <b>{activity?.performedByAccount?.username}</b>
          <div className={styles.label}>
            {t(`taskWorkflowActivityInfoLabel_${activity.type}`)}
          </div>
          <div className={styles.dateDiff} data-tooltip={createdDate}>
            {dateDiff}
          </div>
        </div>
      </Button>

      <Transition initial={true} className={styles.diffContainer}>
        {diffVisible && (
          <>
            {activity.type == "EDIT_TASK_TITLE" && (
              <TitleDiffInfo activity={activity} />
            )}
            {activity.type == "EDIT_TASK_DESC" && (
              <DescDiffInfo activity={activity} />
            )}
            {activity.type == "TASK_UPDATE_TOPIC" && (
              <TopicDiffInfo activity={activity} />
            )}
            {activity.type == "TASK_UPDATE_WORKFLOW_STATUS" && (
              <WorkflowStatusDiffInfo activity={activity} />
            )}
            {activity.type == "TASK_CHANGE_ASSIGNEE" && (
              <AssigneeChangeDiffInfo activity={activity} />
            )}
            {activity.type == "TASK_CHANGE_ASSIGNED_DATE" && (
              <AssignedDateChangeDiffInfo activity={activity} />
            )}
            {activity.type == "TASK_CHANGE_DUE_DATE" && (
              <DueDateChangeDiffInfo activity={activity} />
            )}
            {(activity.type == "RELATION_INITIALIZED" ||
              activity.type == "RELATION_REMOVED") && (
              <TaskRelationChangedDiffInfo activity={activity} />
            )}
          </>
        )}
      </Transition>
      {diffVisible && <div className="spacer-h-1" />}
    </div>
  );
};

export default TaskActivity;
