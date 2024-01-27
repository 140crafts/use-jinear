import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import TaskCreatedToast from "@/components/taskCreatedToast/TaskCreatedToast";
import { IRelatedFeedItemData } from "@/model/app/store/modal/modalState";
import { TaskInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useInitializeTaskMutation } from "@/store/api/taskApi";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { IoInformationCircleOutline } from "react-icons/io5";
import { LuChevronRight } from "react-icons/lu";
import WorkspaceAndTeamInfo from "../common/workspaceAndTeamInfo/WorkspaceAndTeamInfo";
import styles from "./NewTaskForm.module.scss";
import BoardPickerButton from "./boardPickerButton/BoardPickerButton";
import DatePickerButton from "./datePickerButton/DatePickerButton";
import RelatedFeedItemButton from "./relatedFeedItemButton/RelatedFeedItemButton";
import TeamMemberPickerButton from "./teamMemberPickerButton/TeamMemberPickerButton";
import TitleInput from "./titleInput/TitleInput";
import TopicPickerButton from "./topicPickerButton/TopicPickerButton";

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
  onClose,
  className,
  footerContainerClass,
}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    watch,
    reset,
    formState: { errors },
  } = useForm<TaskInitializeRequest>();
  const [selectedTeam, setSelectedTeam] = useState<TeamDto>(initialTeam);
  const workspaceId = workspace.workspaceId;
  const assignedDate = watch("assignedDate");
  const dueDate = watch("dueDate");
  const feedId = watch("feedId");
  const feedItemId = watch("feedItemId");

  const [
    initializeTask,
    { data: initializeTaskResponse, isLoading: isInitializeTaskLoading, isSuccess: isInitializeTaskSuccess },
  ] = useInitializeTaskMutation();

  useEffect(() => {
    reset?.();
    setTimeout(() => {
      setFocus("title");
      setValue("topicId", "no-topic");
      setValue("assignedTo", "no-assignee");
      setValue("boardId", "no-board");
      setValue("teamId", selectedTeam.teamId);
      setValue("feedId", initialRelatedFeedItemData ? initialRelatedFeedItemData.feedId : undefined);
      setValue("feedItemId", initialRelatedFeedItemData ? initialRelatedFeedItemData.feedItemId : undefined);
    }, 200);
  }, []);

  useEffect(() => {
    setSelectedTeam(initialTeam);
  }, [initialTeam]);

  useEffect(() => {
    if (selectedTeam) {
      setValue("teamId", selectedTeam.teamId);
    }
  }, [selectedTeam]);

  useEffect(() => {
    if (isInitializeTaskSuccess && initializeTaskResponse) {
      console.log({ NewTaskForm: initializeTaskResponse });
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
        <input type="hidden" value={workspaceId} {...register("workspaceId")} />
        <input type="hidden" value={selectedTeam?.teamId} {...register("teamId")} />
        {subTaskOf && <input type="hidden" value={subTaskOf} {...register("subTaskOf")} />}

        <div className={styles.workspaceAndTeamInfoContainer}>
          <WorkspaceAndTeamInfo
            readOnly={subTaskOf != null}
            workspace={workspace}
            team={selectedTeam}
            onTeamChange={setSelectedTeam}
            buttonContainerClassName={styles.workspaceAndTeamInfoButtonContainer}
            heightVariant={ButtonHeight.short}
          />
          <div className={styles.newTaskLabelContainer}>
            <LuChevronRight className={styles.titleChevronIcon} />
            <div className="single-line">{t("newTaskModalTitle")}</div>
          </div>
        </div>

        <TitleInput labelClass={styles.label} register={register} />

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

        <div className={styles.actionBar}>
          <TopicPickerButton register={register} setValue={setValue} workspace={workspace} team={selectedTeam} />
          <TeamMemberPickerButton register={register} setValue={setValue} teamId={selectedTeam.teamId} />
          <BoardPickerButton register={register} setValue={setValue} workspace={workspace} team={selectedTeam} />
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
            variant={ButtonVariants.contrast}
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
