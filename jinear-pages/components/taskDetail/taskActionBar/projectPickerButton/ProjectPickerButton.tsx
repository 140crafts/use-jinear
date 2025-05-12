import React, { useEffect } from "react";
import { useTask } from "@/components/taskDetail/context/TaskDetailContext";
import ProjectAndMilestonePickerButton
  from "@/components/projectAndMilestonePickerButton/ProjectAndMilestonePickerButton";
import { MilestoneDto, ProjectDto } from "@/be/jinear-core";
import { useUpdateTaskProjectAnMilestoneMutation } from "@/api/taskUpdateApi";
import { changeLoadingModalVisibility } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "@/locals/useTranslation";

interface ProjectPickerButtonProps {

}

const ProjectPickerButton: React.FC<ProjectPickerButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const task = useTask();
  const [updateTaskProjectAnMilestone, { isLoading: isUpdateProjectAndMilestoneLoading }] = useUpdateTaskProjectAnMilestoneMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isUpdateProjectAndMilestoneLoading }));
  }, [dispatch, isUpdateProjectAndMilestoneLoading]);

  const updateTaskProjectAndMilestone = ({ project, milestone }: { project: ProjectDto, milestone: MilestoneDto }) => {
    if (project.projectId != task.projectId || milestone.milestoneId != task.milestoneId) {
      updateTaskProjectAnMilestone({
        taskId: task.taskId,
        body: { projectId: project.projectId, milestoneId: milestone.milestoneId }
      });
    }
  };

  const removeSelection = () => {
    updateTaskProjectAnMilestone({
      taskId: task.taskId,
      body: { projectId: null, milestoneId: null }
    });
  };

  return !task || !task.workspace || !task.team ? null : (
    <ProjectAndMilestonePickerButton
      workspace={task.workspace}
      team={task.team}
      initialProject={task.project}
      initialMilestone={task.milestone}
      onPick={updateTaskProjectAndMilestone}
      onUnpick={removeSelection}
      withoutUnpickButton={true}
      emptySelectionLabel={t("taskDetailPickProjectAndMilestoneButtonLabel")}
    />
  );
};

export default ProjectPickerButton;