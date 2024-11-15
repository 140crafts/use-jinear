import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TaskCreatedToast from "@/components/taskCreatedToast/TaskCreatedToast";
import { IRelatedFeedItemData } from "@/model/app/store/modal/modalState";
import {
  MilestoneDto,
  ProjectDto,
  TaskBoardDto,
  TaskInitializeRequest,
  TeamDto,
  WorkspaceDto
} from "@/model/be/jinear-core";
import { useInitializeTaskMutation } from "@/store/api/taskApi";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { IoInformationCircleOutline } from "react-icons/io5";
import WorkspaceAndTeamInfo from "../common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import styles from "./NewTaskForm.module.scss";
import BoardPickerButton, { IBoardPickerButtonRef } from "./boardPickerButton/BoardPickerButton";
import DatePickerButton from "./datePickerButton/DatePickerButton";
import RelatedFeedItemButton from "./relatedFeedItemButton/RelatedFeedItemButton";
import TeamMemberPickerButton, { ITeamMemberPickerButtonRef } from "./teamMemberPickerButton/TeamMemberPickerButton";
import TitleInput from "./titleInput/TitleInput";
import TopicPickerButton, { ITopicPickerButtonRef } from "./topicPickerButton/TopicPickerButton";
import ProjectAndMilestonePickerButton, {
  IProjectAndMilestonePickerButtonRef
} from "@/components/form/newTaskForm/projectAndMilestonePickerButton/ProjectAndMilestonePickerButton";

interface NewTaskFormProps {
  workspace: WorkspaceDto;
  initialTeam: TeamDto;
  subTaskOf?: string;
  subTaskOfLabel?: string;
  initialAssignedDate?: Date;
  initialAssignedDateIsPrecise?: boolean;
  initialDueDate?: Date;
  initialDueDateIsPrecise?: boolean;
  initialRelatedFeedItemData?: IRelatedFeedItemData;
  initialProject?: ProjectDto;
  initialMilestone?: MilestoneDto;
  initialBoard?: TaskBoardDto;
  onClose: () => void;
  className?: string;
  footerContainerClass?: string;
}

const logger = Logger("NewTaskForm");

const NewTaskForm: React.FC<NewTaskFormProps> = ({
                                                   workspace,
                                                   initialTeam,
                                                   subTaskOf,
                                                   subTaskOfLabel,
                                                   initialAssignedDate,
                                                   initialAssignedDateIsPrecise,
                                                   initialDueDate,
                                                   initialDueDateIsPrecise,
                                                   initialRelatedFeedItemData,
                                                   initialProject,
                                                   initialMilestone,
                                                   initialBoard,
                                                   onClose,
                                                   className,
                                                   footerContainerClass
                                                 }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    getValues,
    watch,
    reset,
    formState: { errors }
  } = useForm<TaskInitializeRequest>();
  const [selectedTeam, setSelectedTeam] = useState<TeamDto>(initialTeam);
  const workspaceId = workspace.workspaceId;
  const assignedDate = watch("assignedDate");
  const dueDate = watch("dueDate");
  const feedId = watch("feedId");
  const feedItemId = watch("feedItemId");

  const topicPickerButtonRef = useRef<ITopicPickerButtonRef>(null);
  const teamMemberPickerButtonRef = useRef<ITeamMemberPickerButtonRef>(null);
  const boardPickerButtonRef = useRef<IBoardPickerButtonRef>(null);
  const projectPickerButtonRef = useRef<IProjectAndMilestonePickerButtonRef>(null);

  const [
    initializeTask,
    { data: initializeTaskResponse, isLoading: isInitializeTaskLoading, isSuccess: isInitializeTaskSuccess }
  ] = useInitializeTaskMutation();

  useEffect(() => {
    reset?.();
    setTimeout(() => {
      setFocus("title");
      setValue("topicId", "no-topic");
      setValue("assignedTo", "no-assignee");
      setValue("teamId", selectedTeam.teamId);
      setValue("boardId", initialBoard?.taskBoardId ? initialBoard?.taskBoardId : "no-board");
      setValue("feedId", initialRelatedFeedItemData ? initialRelatedFeedItemData.feedId : undefined);
      setValue("feedItemId", initialRelatedFeedItemData ? initialRelatedFeedItemData.feedItemId : undefined);
      setValue("projectId", initialProject ? initialProject.projectId : undefined);
      setValue("milestoneId", initialMilestone ? initialMilestone.milestoneId : undefined);
    }, 200);
  }, []);

  useEffect(() => {
    setSelectedTeam(initialTeam);
  }, [initialTeam]);

  useEffect(() => {
    if (selectedTeam) {
      setValue("teamId", selectedTeam.teamId);
      setValue("topicId", "no-topic");
      setValue("assignedTo", "no-assignee");
      setValue("boardId", "no-board");
      topicPickerButtonRef?.current && topicPickerButtonRef?.current.reset?.();
      teamMemberPickerButtonRef?.current && teamMemberPickerButtonRef?.current.reset?.();
      boardPickerButtonRef?.current && boardPickerButtonRef?.current.reset?.();
      projectPickerButtonRef?.current && projectPickerButtonRef?.current.reset?.();
    }
  }, [selectedTeam, setValue]);

  useEffect(() => {
    if (isInitializeTaskSuccess && initializeTaskResponse) {
      logger.log({ NewTaskForm: initializeTaskResponse });
      if (initializeTaskResponse.data.workspace && initializeTaskResponse.data.team) {
        const teamTag = initializeTaskResponse.data.team?.tag;
        const teamTagNo = initializeTaskResponse.data.teamTagNo;
        const tag = `${teamTag}-${teamTagNo}`;
        const workspaceUsername = initializeTaskResponse.data.workspace?.username;
        const taskLink = `${workspaceUsername}/task/${tag}`;
        toast((t) => <TaskCreatedToast teamTaskNo={tag} taskLink={taskLink} />);
      } else {
        toast(t("genericSuccess"));
      }
      reset?.();
      onClose?.();
    }
  }, [isInitializeTaskSuccess, initializeTaskResponse]);

  useEffect(() => {
    logger.log({ titleErrorMessage: errors.title?.message });
    if (errors.title?.message) {
      toast(errors.title.message, {
        position: window.innerWidth < 768 ? "top-center" : "bottom-center",
        duration: 6000
      });
    }
  }, [errors.title]);

  const submit: SubmitHandler<TaskInitializeRequest> = (data) => {
    if (data.assignedTo == "no-assignee") {
      data.assignedTo = undefined;
    }
    if (data.topicId == "no-topic") {
      data.topicId = undefined;
    }
    if (data.boardId == "no-board") {
      data.boardId = undefined;
    }
    // @ts-ignore
    if (data.assignedDate == "no-date") {
      data.assignedDate = undefined;
    }
    console.log({ NewTaskForm: data });
    if (isInitializeTaskLoading) {
      return;
    }
    initializeTask(data);
  };

  return (
    <form
      autoComplete="off"
      id={"new-task-form"}
      className={cn(styles.form, className)}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>

        <div className={styles.workspaceAndTeamInfoContainer}>
          <WorkspaceAndTeamInfo
            readOnly={subTaskOf != null}
            workspace={workspace}
            team={selectedTeam}
            onTeamChange={setSelectedTeam}
            buttonContainerClassName={styles.workspaceAndTeamInfoButtonContainer}
            heightVariant={ButtonHeight.short}
          />
        </div>

        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={selectedTeam?.teamId} {...register("teamId")} />
        {subTaskOf && <input type="hidden" value={subTaskOf} {...register("subTaskOf")} />}

        {subTaskOfLabel && (
          <>
            <div className={styles.subtaskInfo}>
              <IoInformationCircleOutline />
              <div
                dangerouslySetInnerHTML={{ __html: t("newTaskFormSubtaskOfLabel").replace("${subtaskOfLabel}", subTaskOfLabel) }}
              />
            </div>
            <div className="spacer-h-2" />
          </>
        )}

        {initialRelatedFeedItemData && feedId && feedItemId && (
          <div className={styles.relatedFeedItemInfo}>
            <RelatedFeedItemButton
              initialRelatedFeedItemData={initialRelatedFeedItemData}
              register={register}
              setValue={setValue}
            />
          </div>
        )}

        <TitleInput labelClass={styles.label} register={register} />

        <div className="flex-1" />

        <div className={styles.actionBar}>
          <TopicPickerButton
            register={register}
            setValue={setValue}
            workspace={workspace}
            team={selectedTeam}
            ref={topicPickerButtonRef}
          />
          <TeamMemberPickerButton
            register={register}
            setValue={setValue}
            teamId={selectedTeam.teamId}
            ref={teamMemberPickerButtonRef}
          />

          <ProjectAndMilestonePickerButton
            register={register}
            setValue={setValue}
            workspace={workspace}
            team={selectedTeam}
            ref={projectPickerButtonRef}
            initialProject={initialProject}
            initialMilestone={initialMilestone}
          />

          <BoardPickerButton
            register={register}
            setValue={setValue}
            workspace={workspace}
            team={selectedTeam}
            ref={boardPickerButtonRef}
            initialBoard={initialBoard}
          />

          <DatePickerButton
            register={register}
            setValue={setValue}
            fieldName="assignedDate"
            isPreciseFieldName="hasPreciseAssignedDate"
            initialDate={initialAssignedDate}
            initialDateIsPrecise={initialAssignedDateIsPrecise}
            dateSpanStart={assignedDate && dueDate ? new Date(assignedDate) : undefined}
            dateSpanEnd={dueDate ? new Date(dueDate) : undefined}
            disabledAfter={dueDate ? new Date(dueDate) : undefined}
          />
          <DatePickerButton
            register={register}
            setValue={setValue}
            fieldName="dueDate"
            isPreciseFieldName="hasPreciseDueDate"
            initialDate={initialDueDate}
            initialDateIsPrecise={initialDueDateIsPrecise}
            dateSpanStart={assignedDate ? new Date(assignedDate) : undefined}
            dateSpanEnd={dueDate ? new Date(dueDate) : undefined}
            disabledBefore={assignedDate ? new Date(assignedDate) : undefined}
          />

        </div>

        {/* <DescriptionInput labelClass={styles.label} inputClass={styles.textAreaInput} register={register} setValue={setValue} /> */}
      </div>

      <div className={cn(styles.footerContainer, footerContainerClass)}>
        <div className="flex-1" />
        <div className={styles.footerActionButtonContainer}>
          <Button
            disabled={isInitializeTaskLoading}
            onClick={onClose}
            className={styles.footerButton}
            heightVariant={ButtonHeight.short}
          >
            {t("newTaskModalCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInitializeTaskLoading}
            loading={isInitializeTaskLoading}
            className={styles.footerButton}
            variant={ButtonVariants.brandColor}
            heightVariant={ButtonHeight.short}
            progessClassname={styles.loadingButton}
          >
            {t("newTaskModalCreate")}
          </Button>
        </div>
      </div>
    </form>
  );
};

export default NewTaskForm;
