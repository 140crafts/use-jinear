import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveTaskFeedItemsQuery } from "@/store/api/taskFeedItemApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTask } from "../context/TaskDetailContext";
import styles from "./TaskFeedItemList.module.css";
import FeedContentItem from "./feedContentItem/FeedContentItem";

interface TaskFeedItemListProps {}

const TaskFeedItemList: React.FC<TaskFeedItemListProps> = ({}) => {
  const { t } = useTranslation();
  const task = useTask();
  const taskId = task.taskId;
  const { data: taskFeedItemResponse, isFetching } = useRetrieveTaskFeedItemsQuery({ taskId });
  const feedContentItemList = taskFeedItemResponse?.data.feedContentItemList;
  const totalItemCount = taskFeedItemResponse?.data.totalItemCount;
  const hiddenItemExists = (feedContentItemList?.length || 0) != totalItemCount;

  return feedContentItemList?.length != 0 ? (
    <div className={styles.container}>
      {/* <h2 className={styles.title}>{t("taskDetailFeedsTitle")}</h2> */}
      {isFetching && <CircularLoading />}
      {feedContentItemList?.map?.((feedContentItem) => (
        <FeedContentItem
          key={`task-detail-${feedContentItem.externalId}`}
          feedContentItem={feedContentItem}
          workspace={task.workspace}
        />
      ))}
    </div>
  ) : null;
};

export default TaskFeedItemList;
