import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useToggle } from "@/hooks/useToggle";
import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import decideWorkspaceActivityIcon from "@/utils/workspaceActivityIconMap";
import cn from "classnames";
import { differenceInDays, differenceInHours, differenceInMinutes, format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import { IoCheckmarkCircleSharp, IoRadioButtonOff } from "react-icons/io5";
import styles from "./TaskActivity.module.scss";
import AssignedDateChangeDiffInfo from "./assignedDateChangeDiffInfo/AssignedDateChangeDiffInfo";
import AssigneeChangeDiffInfo from "./assigneeChangeDiffInfo/AssigneeChangeDiffInfo";
import BasicTextDiff from "./basicTextDiff/BasicTextDiff";
import DescDiffInfo from "./descDiffInfo/DescDiffInfo";
import DueDateChangeDiffInfo from "./dueDateChangeDiffInfo/DueDateChangeDiffInfo";
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
  "CHECKLIST_TITLE_CHANGED",
  "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED",
  "CHECKLIST_ITEM_LABEL_CHANGED",
  "CHECKLIST_ITEM_REMOVED",
  "CHECKLIST_ITEM_INITIALIZED",
  "ATTACHMENT_ADDED",
  "ATTACHMENT_DELETED",
  "TASK_BOARD_ENTRY_INIT",
  "TASK_BOARD_ENTRY_REMOVED",
  "TASK_BOARD_ENTRY_ORDER_CHANGE",
];

const TaskActivity: React.FC<TaskActivityProps> = ({ activity }) => {
  const { t } = useTranslation();
  const { current: diffVisible, toggle: toggleDiff } = useToggle();

  const Icon = useMemo(() => decideWorkspaceActivityIcon(activity.type), [activity.workspaceActivityId]);

  const createdDate = format(new Date(activity.createdDate), t("dateTimeFormat"));
  const dateDiff = useMemo(() => {
    const diffInDays = differenceInDays(new Date(), new Date(activity.createdDate));
    if (diffInDays != 0) {
      return t("taskWorkflowActivityInfoLabelDateInDays")?.replace("${num}", `${diffInDays}`);
    }
    const diffInHours = differenceInHours(new Date(), new Date(activity.createdDate));
    if (diffInHours != 0) {
      return t("taskWorkflowActivityInfoLabelDateInHours")?.replace("${num}", `${diffInHours}`);
    }
    const diffInMinutes = differenceInMinutes(new Date(), new Date(activity.createdDate));
    if (diffInMinutes != 0) {
      return t("taskWorkflowActivityInfoLabelDateInMinutes")?.replace("${num}", `${diffInMinutes}`);
    }
    return t("taskWorkflowActivityInfoLabelDateJustNow");
  }, [activity.createdDate]);

  const activityDateLabel = t(`taskWorkflowActivityInfoLabel_${activity.type}`) || "";
  const userName = activity?.performedByAccount?.username;

  return (
    <div className={styles.container}>
      <Button
        onClick={TASK_RELATED_ACTIONS_WITH_DIFF.indexOf(activity.type) != -1 ? toggleDiff : undefined}
        className={styles.contentContainer}
        data-tooltip-right={createdDate}
      >
        <Icon size={15} className={styles.icon} />
        <span className={styles.infoText}>
          <b className={styles.userName}>{`${userName}`}</b>
          {`${activityDateLabel}`}
          <div className="flex-1" />
          <p>{`(${dateDiff})`}</p>
        </span>
      </Button>

      <div
        // initial={true}
        className={cn(
          styles.diffContainer,
          diffVisible && styles.diffContainerVisible,
          activity.type == "EDIT_TASK_DESC" && styles["diffContainer-fullWidth"]
        )}
      >
        {diffVisible && (
          <>
            {activity.type == "EDIT_TASK_TITLE" && <TitleDiffInfo activity={activity} />}
            {activity.type == "EDIT_TASK_DESC" && <DescDiffInfo activity={activity} />}
            {activity.type == "TASK_UPDATE_TOPIC" && <TopicDiffInfo activity={activity} />}
            {activity.type == "TASK_UPDATE_WORKFLOW_STATUS" && <WorkflowStatusDiffInfo activity={activity} />}
            {activity.type == "TASK_CHANGE_ASSIGNEE" && <AssigneeChangeDiffInfo activity={activity} />}
            {activity.type == "TASK_CHANGE_ASSIGNED_DATE" && <AssignedDateChangeDiffInfo activity={activity} />}
            {activity.type == "TASK_CHANGE_DUE_DATE" && <DueDateChangeDiffInfo activity={activity} />}
            {(activity.type == "RELATION_INITIALIZED" || activity.type == "RELATION_REMOVED") && (
              <TaskRelationChangedDiffInfo activity={activity} />
            )}
            {activity.type == "CHECKLIST_TITLE_CHANGED" && (
              <BasicTextDiff oldState={activity.oldState || ""} newState={activity.newState || ""} />
            )}
            {activity.type == "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED" && (
              <BasicTextDiff
                oldState={
                  activity.oldState == "true" ? (
                    <div className={styles.diffStateCell}>
                      <IoCheckmarkCircleSharp size={14} /> {t("taskActivityChecklistStateChecked")}
                    </div>
                  ) : (
                    <div className={styles.diffStateCell}>
                      <IoRadioButtonOff size={14} /> {t("taskActivityChecklistStateUnchecked")}
                    </div>
                  )
                }
                newState={
                  activity.newState == "true" ? (
                    <div className={styles.diffStateCell}>
                      <IoCheckmarkCircleSharp size={14} /> {t("taskActivityChecklistStateChecked")}
                    </div>
                  ) : (
                    <div className={styles.diffStateCell}>
                      <IoRadioButtonOff size={14} /> {t("taskActivityChecklistStateUnchecked")}
                    </div>
                  )
                }
              />
            )}
            {activity.type == "CHECKLIST_ITEM_LABEL_CHANGED" && (
              <BasicTextDiff oldState={activity.oldState || ""} newState={activity.newState || ""} />
            )}
            {activity.type == "CHECKLIST_ITEM_REMOVED" && (
              <BasicTextDiff
                oldState={
                  <div className={cn(styles.diffStateCell, styles.checklistItemRemovedDiffCell)}>
                    {activity.relatedChecklistItem?.isChecked ? (
                      <IoCheckmarkCircleSharp size={14} />
                    ) : (
                      <IoRadioButtonOff size={14} />
                    )}
                    {activity.relatedChecklistItem?.label}
                  </div>
                }
              />
            )}
            {activity.type == "CHECKLIST_ITEM_INITIALIZED" && (
              <BasicTextDiff
                oldState={
                  <div className={cn(styles.diffStateCell)}>
                    {activity.relatedChecklistItem?.isChecked ? (
                      <IoCheckmarkCircleSharp size={14} />
                    ) : (
                      <IoRadioButtonOff size={14} />
                    )}
                    {activity.relatedChecklistItem?.label}
                  </div>
                }
              />
            )}
            {["ATTACHMENT_DELETED", "ATTACHMENT_ADDED"].indexOf(activity.type) != -1 && (
              <BasicTextDiff oldState={activity.relatedTaskMedia?.originalName} />
            )}
            {["TASK_BOARD_ENTRY_INIT", "TASK_BOARD_ENTRY_REMOVED", "TASK_BOARD_ENTRY_ORDER_CHANGE"].indexOf(activity.type) !=
              -1 && (
              <Button
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.filled}
                href={
                  activity.workspaceDto && activity.teamDto
                    ? `/${activity.workspaceDto?.username}/${activity.teamDto?.username}/task-boards/${activity.taskBoard?.taskBoardId}`
                    : undefined
                }
              >
                {activity.taskBoard?.title}
              </Button>
            )}
          </>
        )}
      </div>
      {/* {diffVisible && <div className="spacer-h-1" />} */}
    </div>
  );
};

export default TaskActivity;
