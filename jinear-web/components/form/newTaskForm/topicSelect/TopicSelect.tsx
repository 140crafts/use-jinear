import { TaskInitializeRequest } from "@/model/be/jinear-core";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
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

const TopicSelect: React.FC<TopicSelectProps> = ({
  teamId,
  setValue,
  register,
  labelClass,
  loadingClass,
  selectClass,
}) => {
  const { t } = useTranslation();
  const {
    data: teamTopicListingResponse,
    isSuccess: isTeamTopicListingSuccess,
    isError: isTeamTopicListingError,
    isLoading: isTeamTopicListingLoading,
  } = useRetrieveTeamTopicsQuery(teamId, { skip: teamId == null });

  useEffect(() => {
    if (!teamTopicListingResponse?.data?.hasContent) {
      setValue("topicId", "no-topic");
    } else {
      setValue("topicId", teamTopicListingResponse?.data?.content?.[0].topicId);
    }
  }, [teamTopicListingResponse]);

  return isTeamTopicListingLoading ? (
    <div className={cn(labelClass, styles.topicLabel, loadingClass)}>
      <CircularProgress size={18} />
    </div>
  ) : (
    <label
      className={labelClass}
      htmlFor="task-topic-id"
      data-tooltip={
        !teamTopicListingResponse?.data?.hasContent
          ? t("newTaskModalTaskTopicNoContentTooltip")
          : undefined
      }
    >
      {t("newTaskModalTaskTopicLabel")}
      <select
        disabled={!teamTopicListingResponse?.data?.hasContent}
        id="task-topic-id"
        className={selectClass}
        {...register("topicId")}
      >
        <option value={"no-topic"}>
          {t("newTaskModalTaskTopicNoContentValue")}
        </option>

        {teamTopicListingResponse?.data?.content.map((topic) => (
          <option key={topic.topicId} value={topic.topicId}>
            {topic.name}
          </option>
        ))}
      </select>
    </label>
  );
};

export default TopicSelect;
