import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import { popNewTopicModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./TopicSelect.module.css";

interface TopicSelectProps {
  teamId: string;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
  labelClass: string;
  loadingClass: string;
  selectClass: string;
}

const TopicSelect: React.FC<TopicSelectProps> = ({ teamId, setValue, register, labelClass, loadingClass, selectClass }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const {
    data: teamTopicListingResponse,
    isSuccess: isTeamTopicListingSuccess,
    isError: isTeamTopicListingError,
    isLoading: isTeamTopicListingLoading,
  } = useRetrieveTeamTopicsQuery(teamId, { skip: teamId == null });

  const hasAnyTopicDefined = teamTopicListingResponse?.data?.hasContent;

  useEffect(() => {
    if (!hasAnyTopicDefined) {
      setValue("topicId", "no-topic");
    } else {
      setValue("topicId", teamTopicListingResponse?.data?.content?.[0].topicId);
    }
  }, [teamTopicListingResponse]);

  const popCreateNewTopicModal = () => {
    dispatch(popNewTopicModal());
  };

  return isTeamTopicListingLoading ? (
    <div className={cn(labelClass, styles.topicLabel, loadingClass)}>
      <CircularProgress size={18} />
    </div>
  ) : (
    <div className={styles.selectContainer}>
      <label
        className={labelClass}
        htmlFor="task-topic-id"
        data-tooltip={!hasAnyTopicDefined ? t("newTaskModalTaskTopicNoContentTooltip") : undefined}
      >
        {t("newTaskModalTaskTopicLabel")}
        <select disabled={!hasAnyTopicDefined} id="task-topic-id" className={selectClass} {...register("topicId")}>
          <option value={"no-topic"}>{t("newTaskModalTaskTopicNoContentValue")}</option>

          {teamTopicListingResponse?.data?.content.map((topic) => (
            <option key={topic.topicId} value={topic.topicId}>
              {topic.name}
            </option>
          ))}
        </select>
      </label>
      <Button
        className={styles.newTopicButton}
        heightVariant={ButtonHeight.short}
        variant={ButtonVariants.filled}
        onClick={popCreateNewTopicModal}
      >
        {t("newTaskModalNewTopicButton")}
      </Button>
    </div>
  );
};

export default TopicSelect;
