import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { MilestoneDto, ProjectDto, TaskInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popProjectAndMilestonePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./ProjectAndMilestonePickerButton.module.css";
import { LuBox, LuChevronRight, LuDiamond } from "react-icons/lu";

interface ProjectAndMilestonePickerButton {
  workspace: WorkspaceDto;
  team: TeamDto;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
  initialProject?: ProjectDto;
  initialMilestone?: MilestoneDto;
}

export interface IProjectAndMilestonePickerButtonRef {
  reset: () => void;
}

const logger = Logger("ProjectAndMilestonePickerButton");

const ProjectAndMilestonePickerButton = ({
                                           workspace,
                                           team,
                                           register,
                                           setValue,
                                           initialProject,
                                           initialMilestone
                                         }: ProjectAndMilestonePickerButton, ref: any) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedProject, setSelectedProject] = useState<ProjectDto | undefined>(initialProject);
  const [selectedMilestone, setSelectedMilestone] = useState<MilestoneDto | undefined>(initialMilestone);

  logger.log({ selectedProject, selectedMilestone, initialProject, initialMilestone });

  useEffect(() => {
    setValue("projectId", selectedProject?.projectId);
    setValue("milestoneId", selectedMilestone?.milestoneId);
  }, [selectedProject, selectedMilestone, setValue]);

  useEffect(() => {
    setSelectedProject(initialProject);
    setSelectedMilestone(initialMilestone);
  }, [initialProject, initialMilestone]);

  useImperativeHandle(ref, () => ({
    reset: () => {
      setSelectedProject(initialProject);
      setSelectedMilestone(initialMilestone);
    }
  }));

  const onModalPick = ({ project, milestone }: { project: ProjectDto, milestone: MilestoneDto }) => {
    logger.log({ project, milestone });
    setSelectedProject(project);
    setSelectedMilestone(milestone);
  };

  const reset = () => {
    setSelectedProject(undefined);
    setSelectedMilestone(undefined);
  };

  const popProjectMilestonePicker = () => {
    dispatch(popProjectAndMilestonePickerModal({
      visible: true,
      workspaceId: workspace.workspaceId,
      initialProject: selectedProject,
      initialMilestone: selectedMilestone,
      onPick: onModalPick,
      onUnpick: reset
    }));
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        id={"project-and-milestone-picker-button-select-deselect"}
        hasSelection={selectedProject != null && selectedMilestone != null}
        onPickClick={popProjectMilestonePicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <div className={styles.infoContainer}>
              <LuBox className={"icon"} />
              <span className={"line-clamp"}>{selectedProject?.title}</span>
            </div>
            <LuChevronRight className={"icon"} />
            <div className={styles.infoContainer}>
              <LuDiamond className={"icon"} />
              <span className={"line-clamp"}>{selectedMilestone?.title}</span>
            </div>
          </div>
        }
        emptySelectionLabel={t("newTaskFormPickProjectAndMilestoneButtonLabel")}
        onUnpickClick={reset}
      />
      <input type="hidden" value={selectedProject?.projectId} {...register("projectId")} />
      <input type="hidden" value={selectedMilestone?.milestoneId} {...register("milestoneId")} />
    </div>
  );
};

export default forwardRef<IProjectAndMilestonePickerButtonRef, ProjectAndMilestonePickerButton>(ProjectAndMilestonePickerButton);