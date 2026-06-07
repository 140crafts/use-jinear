import React, { useRef } from "react";
import styles from "./NewProjectForm.module.scss";
import useTranslation from "@/locals/useTranslation";
import { SubmitHandler, useForm } from "react-hook-form";
import {
  ProjectInitializeRequest,
  ProjectPriorityType,
  ProjectStateType,
  TeamDto,
  WorkspaceDto,
  WorkspaceMemberDto
} from "@/be/jinear-core";
import cn from "classnames";
import Logger from "@/utils/logger";
import TitleInput from "@/components/form/newProjectForm/titleInput/TitleInput";
import WorkspaceAndTeamInfo from "@/components/form/common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TeamPickerButton from "@/components/teamPickerButton/TeamPickerButton";
import WorkspaceMemberPickerButton from "@/components/workspaceMemberPickerButton/WorkspaceMemberPickerButton";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import { IoPlaySkipBackOutline, IoPlaySkipForwardOutline } from "react-icons/io5";
import ProjectPriorityPickerButton from "@/components/projectPriorityPickerButton/ProjectPriorityPickerButton";
import ProjectStatePickerButton from "@/components/projectStatePickerButton/ProjectStatePickerButton";
import MilestoneList, { IMilestoneInitializeDto } from "@/components/form/newProjectForm/milestoneList/MilestoneList";
import Line from "@/components/line/Line";
import DescriptionInput from "@/components/form/newProjectForm/descriptionInput/DescriptionInput";
import { ITiptapRef } from "@/components/tiptap/Tiptap";
import { useInitializeProjectMutation } from "@/api/projectOperationApi";
import toast from "react-hot-toast";

interface NewProjectFormProps {
  workspace: WorkspaceDto;
  className?: string;
  footerContainerClass?: string,
  onClose: () => void;
}

const logger = Logger("NewProjectForm");

const NewProjectForm: React.FC<NewProjectFormProps> = ({
                                                         workspace,
                                                         className,
                                                         footerContainerClass,
                                                         onClose
                                                       }) => {
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    getValues,
    watch,
    reset,
    formState: { errors }
  } = useForm<ProjectInitializeRequest>();
  const [initializeProject, { isLoading: isInitializeProjectLoading }] = useInitializeProjectMutation();
  const startDate = watch("startDate");
  const targetDate = watch("targetDate");
  const milestoneList = watch("milestones") as (IMilestoneInitializeDto[] | null);
  const descriptionRef = useRef<ITiptapRef>(null);

  const onTeamPick = (pickedList: TeamDto[]) => {
    logger.log({ onTeamPick: pickedList });
    setValue("teamIds", pickedList?.map(t => t.teamId));
  };

  const onLeadPick = (selection: WorkspaceMemberDto[]) => {
    if (selection && selection?.[0]) {
      setValue("leadWorkspaceMemberId", selection[0].workspaceMemberId);
    }
  };

  const onProjectStartDateChange = (date?: Date | null) => {
    setValue("startDate", date);
  };

  const onProjectTargetDateChange = (date?: Date | null) => {
    setValue("targetDate", date);
  };

  const onPriorityPick = (priority?: ProjectPriorityType | null) => {
    logger.log({ onPriorityPick: priority });
    setValue("projectPriority", priority);
  };

  const onStatePick = (state?: ProjectStateType | null) => {
    logger.log({ onStatePick: state });
    setValue("projectState", state);
  };

  const submit: SubmitHandler<ProjectInitializeRequest> = (data) => {
    const description = descriptionRef.current?.getHTML();
    logger.log({ submit: { ...data, description } });
    const req = { ...data };
    if (data.teamIds == null || data.teamIds.length == 0) {
      toast(t("newProjectFormSelectAtLeastATeam"));
      return;
    }
    initializeProject(req);
    onClose?.();
  };

  return (
    <form
      autoComplete="off"
      id={"new-project-form"}
      className={cn(styles.form, className)}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>

        <input type="hidden" value={workspace.workspaceId} {...register("workspaceId")} />

        <div className={styles.titleAndActionBarContainer}>
          <div className={styles.workspaceAndTeamInfoContainer}>
            <WorkspaceAndTeamInfo
              readOnly={true}
              workspace={workspace}
              buttonContainerClassName={styles.workspaceAndTeamInfoButtonContainer}
              heightVariant={ButtonHeight.short}
              additionalLabel={t("newProjectModalTitle")}
            />
          </div>

          <TitleInput register={register} labelClass={styles.label} />

          <div className={styles.actionBarContainer}>
            <DatePickerButton
              label={t("newProjectFormStartDate")}
              icon={IoPlaySkipForwardOutline}
              onDateChange={onProjectStartDateChange}
              disabledAfter={targetDate}
            />

            <DatePickerButton
              label={t("newProjectFormTargetDate")}
              icon={IoPlaySkipBackOutline}
              onDateChange={onProjectTargetDateChange}
              disabledBefore={startDate}
            />

            <WorkspaceMemberPickerButton
              workspaceId={workspace.workspaceId}
              multiple={false}
              onPick={onLeadPick}
              label={t("newTaskFormLeadWorkspaceMemberPickerButtonLabel")}
            />

            <TeamPickerButton
              workspaceId={workspace.workspaceId}
              multiple={true}
              onPick={onTeamPick}
              useJoinedNameOnMultiplePick={true}
              label={t("newProjectFormRelatedTeamPickerButtonLabel")}
            />

            <ProjectPriorityPickerButton
              onPick={onPriorityPick}
              initialPick={"NONE"}
              withoutUnpickButton={true}
            />

            <ProjectStatePickerButton
              onPick={onStatePick}
              initialPick={"BACKLOG"}
              withoutUnpickButton={true}
            />
          </div>
        </div>

        <Line />
        <MilestoneList milestoneList={milestoneList} setValue={setValue} />
        <Line />
        <DescriptionInput tiptapRef={descriptionRef} register={register} />
      </div>

      <div className={cn(styles.footerContainer, footerContainerClass)}>
        <div className="flex-1" />
        <div className={styles.footerActionButtonContainer}>
          <Button
            disabled={isInitializeProjectLoading}
            onClick={onClose}
            className={styles.footerButton}
            heightVariant={ButtonHeight.short}
          >
            {t("newProjectModalCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInitializeProjectLoading}
            loading={isInitializeProjectLoading}
            className={styles.footerButton}
            variant={ButtonVariants.contrast}
            heightVariant={ButtonHeight.short}
            progessClassname={styles.loadingButton}
          >
            {t("newProjectModalCreate")}
          </Button>
        </div>
      </div>
    </form>
  );
};

export default NewProjectForm;