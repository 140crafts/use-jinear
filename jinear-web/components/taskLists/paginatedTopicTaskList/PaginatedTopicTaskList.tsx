import { useRetrieveFromTopicQuery } from "@/store/api/taskListingApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PaginatedTopicTaskListProps {
  teamId: string;
  workspaceId: string;
  topicTag: string;
}

const PaginatedTopicTaskList: React.FC<PaginatedTopicTaskListProps> = ({ teamId, workspaceId, topicTag }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const {
    data: response,
    isSuccess,
    isError,
    isFetching,
    isLoading,
  } = useRetrieveFromTopicQuery({
    teamId,
    workspaceId,
    topicTag,
    page,
  });

  return (
    <BaseTaskList
      id={`topic-tasks-${workspaceId}-${teamId}-${topicTag}`}
      name={t("topicTaskListName").replace("${topicTag}", topicTag)}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default PaginatedTopicTaskList;
