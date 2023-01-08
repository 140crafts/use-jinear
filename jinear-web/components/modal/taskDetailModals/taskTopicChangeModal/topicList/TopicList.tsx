import Button, { ButtonVariants } from "@/components/button";
import { useUpdateTaskTopicMutation } from "@/store/api/taskTopicApi";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import {
  changeLoadingModalVisibility,
  selectChangeTaskTopicModalTaskCurrentTopicId,
  selectChangeTaskTopicModalTaskId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./TopicList.module.css";

interface TopicListProps {
  teamId: string;
  filter: string;
  close: () => void;
}

const TopicList: React.FC<TopicListProps> = ({ teamId, filter, close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const taskId = useTypedSelector(selectChangeTaskTopicModalTaskId);
  const taskCurrentTopicId = useTypedSelector(
    selectChangeTaskTopicModalTaskCurrentTopicId
  );
  const { data: retrieveTeamTopicListResponse, isFetching } =
    useRetrieveTeamTopicsQuery(teamId, {
      skip: teamId == null,
    });

  const [
    updateTaskTopic,
    { isSuccess: isUpdateSuccess, isError: isUpdateError },
  ] = useUpdateTaskTopicMutation();

  useEffect(() => {
    if (isUpdateSuccess || isUpdateError) {
      dispatch(changeLoadingModalVisibility({ visible: false }));
      close();
    }
  }, [isUpdateSuccess, isUpdateError]);

  const changeTaskTopic = (nextTopicId: string) => {
    if (taskCurrentTopicId == nextTopicId) {
      close();
      return;
    }
    if (taskId) {
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateTaskTopic({ taskId, topicId: nextTopicId });
    }
  };

  const filteredList =
    retrieveTeamTopicListResponse?.data.content.filter(
      (topic) =>
        filter == "" ||
        topic.name?.toLowerCase().indexOf(filter?.toLowerCase()) != -1
    ) || [];

  return (
    <div className={styles.container}>
      {isFetching && (
        <div className={styles.centeredInfo}>
          <CircularProgress size={17} />
        </div>
      )}

      {filteredList.map((topic) => (
        <Button
          key={`task-topic-modal-${topic.topicId}`}
          className={styles.button}
          variant={
            taskCurrentTopicId == topic.topicId
              ? ButtonVariants.filled2
              : ButtonVariants.filled
          }
          onClick={() => changeTaskTopic(topic.topicId)}
        >
          {topic.name}
        </Button>
      ))}

      {filteredList.length == 0 && !isFetching && (
        <div className={styles.centeredInfo}>
          {t("changeTaskTopicModalFilteredListEmpty")}
        </div>
      )}
    </div>
  );
};

export default TopicList;
