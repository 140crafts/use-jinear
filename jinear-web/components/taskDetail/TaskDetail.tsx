import { useToggle } from "@/hooks/useToggle";
import { TaskDto } from "@/model/be/jinear-core";
import { hasWorkspaceFilePermissions } from "@/utils/permissionHelper";
import useTranslation from "locales/useTranslation";
import React from "react";
import LastTaskActivitiesList from "../lastActivitiesScreen/lastTaskActivitiesList/LastTaskActivitiesList";
import Line from "../line/Line";
import styles from "./TaskDetail.module.css";
import TaskDetailContext from "./context/TaskDetailContext";
import TaskActionBar from "./taskActionBar/TaskActionBar";
import TaskBody from "./taskBody/TaskBody";
import TaskChecklistContainer from "./taskChecklistContainer/TaskChecklistContainer";
import TaskHasUpdatesInfo from "./taskHasUpdatesInfo/TaskHasUpdatesInfo";
import TaskMediaList from "./taskMediaList/TaskMediaList";
import TaskSubtaskList from "./taskSubtaskList/TaskSubtaskList";

interface TaskDetailProps {
  task: TaskDto;
}

const TaskDetail: React.FC<TaskDetailProps> = ({ task }) => {
  const { t } = useTranslation();
  const { current: showSubTaskListEvenIfNoSubtasks, toggle: toggleShowSubTaskListEvenIfNoSubtasks } = useToggle(false);

  return (
    <TaskDetailContext.Provider
      value={{
        task,
        showSubTaskListEvenIfNoSubtasks,
        toggleShowSubTaskListEvenIfNoSubtasks,
      }}
    >
      <div className={styles.taskLayout}>
        <TaskHasUpdatesInfo />
        <TaskBody className={styles.taskBody} />
        <Line />
        <TaskActionBar className={styles.taskInfo} />
        <TaskChecklistContainer />
        {hasWorkspaceFilePermissions(task.workspace) && (
          <>
            <Line />
            <TaskMediaList />
          </>
        )}
        <TaskSubtaskList />
        <Line />
        <LastTaskActivitiesList
          workspaceId={task.workspaceId}
          taskId={task.taskId}
          listTitle={t("taskActivityListTitle")}
          listTitleClassName={styles.taskActivitiesTitle}
        />
      </div>
    </TaskDetailContext.Provider>
  );
};

export default TaskDetail;
