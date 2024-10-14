import React, { Dispatch, SetStateAction, useState } from "react";
import styles from "./PostCommentList.module.css";
import { useListProjectPostCommentsQuery } from "@/api/projectPostCommentApi";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import { ProjectPostCommentDto } from "@/be/jinear-core";
import CommentSimple from "@/components/projectFeedPostDetailScreen/postCommentSimple/CommentSimple";
import useTranslation from "@/locals/useTranslation";

interface PostCommentListProps {
  projectId: string,
  postId: string,
  setQuotedComment?: Dispatch<SetStateAction<ProjectPostCommentDto | undefined>>;
  canComment: boolean
  hasExplicitAdminDeleteAccess: boolean
}

const PostCommentList: React.FC<PostCommentListProps> = ({
                                                           projectId,
                                                           postId,
                                                           setQuotedComment,
                                                           canComment,
                                                           hasExplicitAdminDeleteAccess
                                                         }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);
  const { data: projectCommentsResponse, isLoading, isFetching } = useListProjectPostCommentsQuery({
    projectId,
    postId,
    page
  });

  const renderItem = (item: ProjectPostCommentDto) => {
    return <CommentSimple
      key={item.projectPostCommentId}
      comment={item}
      setQuotedComment={setQuotedComment}
      canComment={canComment}
      hasExplicitAdminDeleteAccess={hasExplicitAdminDeleteAccess}
    />;
  };

  return (
    <PaginatedList
      id={"post-comment-list"}
      data={projectCommentsResponse?.data}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
      renderItem={renderItem}
      emptyLabel={t("projectPostCommentListEmptyLabel")}
      hidePaginationOnSinglePages={true}
      contentContainerClassName={styles.list}
      listTitle={t("projectPostCommentListTitle")}
    />
  );
};

export default PostCommentList;