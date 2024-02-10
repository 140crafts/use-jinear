import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import { useToggle } from "@/hooks/useToggle";
import { CommentDto } from "@/model/be/jinear-core";
import { useRetrieveTaskCommentsQuery } from "@/store/api/taskCommentApi";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { useTask } from "../context/TaskDetailContext";
import styles from "./TaskComments.module.css";
import CommentInput from "./commentInput/CommentInput";
import CommentSimple from "./commentSimple/CommentSimple";

interface TaskCommentsProps {}

const TaskComments: React.FC<TaskCommentsProps> = ({}) => {
  const { t } = useTranslation();
  const task = useTask();
  const [page, setPage] = useState<number>(0);
  const [quotedComment, setQuotedComment] = useState<CommentDto>();
  const [newCommentInputVisible, toggleNewCommentInputVisible] = useToggle(false);

  const { data: response, isLoading, isFetching } = useRetrieveTaskCommentsQuery({ taskId: task.taskId, page });

  useEffect(() => {
    if (quotedComment) {
      toggleNewCommentInputVisible();
    }
  }, [quotedComment]);

  const renderItem = (data: CommentDto, index: number) => {
    return (
      <CommentSimple
        key={data.commentId}
        comment={data}
        setQuotedComment={setQuotedComment}
        withDivider={index + 1 != response?.data.content.length}
      />
    );
  };

  return (
    <div className={styles.container}>
      <PaginatedList
        id={`task-comments-${task.taskId}`}
        data={response?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        // emptyLabel={t("taskCommentListEmpty")}
        emptyLabel={""}
        listTitle={t("taskCommentsTitle")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
        listTitleClassName={styles.listTitle}
        paginationPosition="bottom"
      />
      {newCommentInputVisible ? (
        <CommentInput
          taskId={task.taskId}
          quotedComment={quotedComment}
          setQuotedComment={setQuotedComment}
          toggleInput={toggleNewCommentInputVisible}
        />
      ) : (
        <Button
          className={styles.newCommentButton}
          variant={ButtonVariants.filled}
          heightVariant={ButtonHeight.short}
          onClick={toggleNewCommentInputVisible}
        >
          {t("taskDetailCommentsNewComment")}
        </Button>
      )}
    </div>
  );
};

export default TaskComments;
