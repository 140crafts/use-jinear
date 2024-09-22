import React from "react";
import styles from "./ProjectActionButtons.module.css";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";
import { ProjectDto, ProjectPriorityType, ProjectStateType, TeamDto, WorkspaceMemberDto } from "@/be/jinear-core";
import WorkspaceMemberPickerButton from "@/components/workspaceMemberPickerButton/WorkspaceMemberPickerButton";
import {
  useUpdateProjectArchivedMutation,
  useUpdateProjectDatesMutation,
  useUpdateProjectLeadMutation,
  useUpdateProjectPriorityMutation,
  useUpdateProjectStateMutation
} from "@/api/projectOperationApi";
import TeamPickerButton from "@/components/teamPickerButton/TeamPickerButton";
import { useUpdateAsProjectTeamsMutation } from "@/api/projectTeamApi";
import ProjectPriorityPickerButton from "@/components/projectPriorityPickerButton/ProjectPriorityPickerButton";
import ProjectStatePickerButton from "@/components/projectStatePickerButton/ProjectStatePickerButton";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { closeDialogModal, popDialogModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { LuArchive } from "react-icons/lu";

interface ProjectActionButtonsProps {
  project: ProjectDto;
  isFetching: boolean;
}

const ProjectActionButtons: React.FC<ProjectActionButtonsProps> = ({ project, isFetching }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const [updateProjectDates, { isLoading: isUpdateProjectDatesLoading }] = useUpdateProjectDatesMutation();
  const [updateProjectLead, { isLoading: isUpdateProjectLeadLoading }] = useUpdateProjectLeadMutation();
  const [updateAsProjectTeams, { isLoading: isUpdateRelatedTeamsLoading }] = useUpdateAsProjectTeamsMutation();
  const [updateProjectPriority, {}] = useUpdateProjectPriorityMutation();
  const [updateProjectState, {}] = useUpdateProjectStateMutation();
  const [updateProjectArchived, {}] = useUpdateProjectArchivedMutation();

  const onProjectStartDateChange = (date?: Date | null) => {
    updateProjectDates({
      projectId: project.projectId,
      body: {
        startDate: date,
        updateStartDate: true
      }
    });
  };

  const onProjectTargetDateChange = (date?: Date | null) => {
    updateProjectDates({
      projectId: project.projectId,
      body: {
        targetDate: date,
        updateTargetDate: true
      }
    });
  };

  const onLeadPick = (selection: WorkspaceMemberDto[]) => {
    if (selection && selection?.[0]) {
      updateProjectLead({
        projectId: project.projectId,
        body: { workspaceMemberId: selection?.[0]?.workspaceMemberId }
      });
    }
  };

  const onLeadDeselect = () => {
    updateProjectLead({
      projectId: project.projectId,
      body: { workspaceMemberId: null }
    });
  };

  const onTeamPick = (pickedList: TeamDto[]) => {
    const teamIds = pickedList?.map(p => p.teamId) ?? [];
    updateAsProjectTeams({ projectId: project.projectId, teamIds });
  };

  const onPriorityPick = (projectPriority?: ProjectPriorityType | null) => {
    if (projectPriority) {
      updateProjectPriority({ projectId: project.projectId, body: { projectPriority } });
    }
  };

  const onStatePick = (projectState?: ProjectStateType | null) => {
    if (projectState) {
      updateProjectState({ projectId: project.projectId, body: { projectState } });
    }
  };

  const popAreYouSureModalForArchiveProject = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("updateProjectAsArchivedTitle"),
        content: t("updateProjectAsArchivedText"),
        confirmButtonLabel: t("updateProjectAsArchivedConfirm"),
        onConfirm: updateAsArchived
      })
    );
  };

  const updateAsArchived = () => {
    dispatch(closeDialogModal());
    updateProjectArchived({ projectId: project.projectId, body: { archived: true } });
  };

  return (
    <div className={styles.container}>
      <DatePickerButton
        label={t("projectDetailSetStartDate")}
        selectedLabel={t("projectDetailStartDate")}
        omitIcon={true}
        onDateChange={onProjectStartDateChange}
        disabledAfter={project.targetDate ? new Date(project.targetDate) : undefined}
        initialDate={project.startDate ? new Date(project.startDate) : undefined}
        unpickableFromModal={true}
        disabled={isUpdateProjectDatesLoading || isFetching}
      />

      <DatePickerButton
        label={t("projectDetailSetTargetDate")}
        selectedLabel={t("projectDetailTargetDate")}
        omitIcon={true}
        onDateChange={onProjectTargetDateChange}
        disabledBefore={project.startDate ? new Date(project.startDate) : undefined}
        initialDate={project.targetDate ? new Date(project.targetDate) : undefined}
        unpickableFromModal={true}
        disabled={isUpdateProjectDatesLoading || isFetching}
      />

      <WorkspaceMemberPickerButton
        workspaceId={project.workspace.workspaceId}
        multiple={false}
        onPick={onLeadPick}
        initialSelectedMembers={project.leadWorkspaceMember ? [project.leadWorkspaceMember] : undefined}
        label={t("newTaskFormLeadWorkspaceMemberPickerButtonLabel")}
        deselectableFromModal={true}
        onDeselect={onLeadDeselect}
        selectionLabel={t("projectDetailLead")}
      />

      <TeamPickerButton
        workspaceId={project.workspaceId}
        multiple={true}
        onPick={onTeamPick}
        useJoinedNameOnMultiplePick={true}
        label={t("projectDetailRelatedTeams")}
        selectedLabel={t("projectDetailRelatedTeams")}
        initialSelectedTeams={project.projectTeams?.map(projectTeam => projectTeam.team)}
        withoutUnpickButton={true}
      />

      <ProjectPriorityPickerButton
        onPick={onPriorityPick}
        initialPick={project.projectPriority}
        withoutUnpickButton={true}
      />

      <ProjectStatePickerButton
        onPick={onStatePick}
        initialPick={project.projectState}
        withoutUnpickButton={true}
      />

      {!project.archived &&
        <Button variant={ButtonVariants.filled}
                heightVariant={ButtonHeight.short}
                onClick={popAreYouSureModalForArchiveProject}
        >
          <LuArchive className={"icon"} />
          <div className={"spacer-w-1"} />
          {t("projectUpdateAsArchived")}
        </Button>}
    </div>
  );
};

export default ProjectActionButtons;