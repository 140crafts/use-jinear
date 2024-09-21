import React from "react";
import styles from "./MilestoneTasks.module.css";
import PrefilteredPaginatedTaskList
  from "@/components/taskLists/prefilteredPaginatedTaskList/PrefilteredPaginatedTaskList";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuCheckSquare, LuPenSquare, LuSearch } from "react-icons/lu";
import { useAppDispatch } from "@/store/store";
import { closeSearchTaskModal, popNewTaskModal, popSearchTaskModal } from "@/slice/modalSlice";
import { MilestoneDto, ProjectDto, TaskDto } from "@/be/jinear-core";
import { useUpdateTaskProjectAnMilestoneMutation } from "@/api/taskUpdateApi";

interface MilestoneTasksProps {
  workspaceId: string;
  milestone: MilestoneDto,
  project: ProjectDto;
}

const MilestoneTasks: React.FC<MilestoneTasksProps> = ({ workspaceId, project, milestone }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const milestoneId = milestone.milestoneId;
  const [updateTaskProjectAnMilestone, {}] = useUpdateTaskProjectAnMilestoneMutation();

  const pickTask = () => {
    const teamIds = project.projectTeams.map(team => team.teamId);
    dispatch(
      popSearchTaskModal({
        workspaceId: workspaceId,
        teamIds,
        onSelect: onTaskSelect,
        visible: true
      })
    );
  };

  const onTaskSelect = (task: TaskDto) => {
    if (task) {
      dispatch(closeSearchTaskModal());
      updateTaskProjectAnMilestone({ taskId: task.taskId, body: { projectId: project.projectId, milestoneId } });
    }
  };

  const onNewTaskClicked = () => {
    if (project.projectTeams.length != 0) {
      const projectFirstTeam = project.projectTeams[0]?.team;
      dispatch(popNewTaskModal({
        visible: true,
        workspace: project.workspace,
        team: projectFirstTeam,
        initialProject: project,
        initialMilestone: milestone
      }));
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <h3 className={"flex-1"}>{t("milestoneTasks")}</h3>
        <Button
          disabled={project.projectTeams.length == 0}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={pickTask}
        >
          <LuSearch className={"icon"} />
        </Button>
        <Button
          disabled={project.projectTeams.length == 0}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={onNewTaskClicked}
        >
          <LuPenSquare className={"icon"} />
        </Button>
      </div>

      <PrefilteredPaginatedTaskList
        id={`project-milestone-task-list-${milestoneId}`}
        filter={{
          workspaceId: workspaceId,
          milestoneIds: [milestoneId]
        }}
      />
    </div>
  );
};

export default MilestoneTasks;