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
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";

interface ProjectAndMilestonePickerButton {
  workspace: WorkspaceDto;
  team: TeamDto;
  initialProject?: ProjectDto | null;
  initialMilestone?: MilestoneDto | null;
  onPick?: ({ project, milestone }: { project: ProjectDto, milestone: MilestoneDto }) => void;
  onUnpick: () => void;
  withoutUnpickButton?: boolean;
  emptySelectionLabel: string;
}

export interface IProjectAndMilestonePickerButtonRef {
  reset: () => void;
}

const logger = Logger("BoardPickerButton");

const MAX_LENGTH = 14;

const ProjectAndMilestonePickerButton = ({
                                           workspace,
                                           team,
                                           initialProject,
                                           initialMilestone,
                                           onPick,
                                           onUnpick,
                                           withoutUnpickButton,
                                           emptySelectionLabel
                                         }: ProjectAndMilestonePickerButton, ref: any) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedProject, setSelectedProject] = useState<ProjectDto | null>();
  const [selectedMilestone, setSelectedMilestone] = useState<MilestoneDto | null>();

  useEffect(() => {
    setSelectedProject(initialProject);
    setSelectedMilestone(initialMilestone);
  }, [initialProject, initialMilestone]);

  useImperativeHandle(ref, () => ({
    reset: () => {
      setSelectedProject(undefined);
      setSelectedMilestone(undefined);
    }
  }));

  const onModalPick = ({ project, milestone }: { project: ProjectDto, milestone: MilestoneDto }) => {
    logger.log({ project, milestone });
    setSelectedProject(project);
    setSelectedMilestone(milestone);
    onPick?.({ project, milestone });
  };

  const reset = () => {
    setSelectedProject(undefined);
    setSelectedMilestone(undefined);
    onUnpick?.();
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
        hasSelection={selectedProject != null && selectedMilestone != null}
        onPickClick={popProjectMilestonePicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <div className={styles.infoContainer}
                 data-tooltip-multiline={selectedProject?.title && selectedProject?.title?.length > MAX_LENGTH ? selectedProject?.title : undefined}
            >
              <LuBox className={"icon"} />
              <span className={"line-clamp"}>{
                shortenStringIfMoreThanMaxLength({
                  text: selectedProject?.title || "",
                  maxLength: 14
                })}</span>
            </div>
            <b>{">"}</b>
            <div className={styles.infoContainer}
                 data-tooltip-multiline={selectedMilestone?.title && selectedMilestone?.title?.length > MAX_LENGTH ? selectedMilestone?.title : undefined}
            >
              <LuDiamond className={"icon"} />
              <span className={"line-clamp"}>
                {shortenStringIfMoreThanMaxLength({
                  text: selectedMilestone?.title || "",
                  maxLength: MAX_LENGTH
                })}</span>
            </div>
          </div>
        }
        emptySelectionLabel={emptySelectionLabel}
        onUnpickClick={reset}
        withoutUnpickButton
      />
    </div>
  );
};

export default forwardRef<IProjectAndMilestonePickerButtonRef, ProjectAndMilestonePickerButton>(ProjectAndMilestonePickerButton);