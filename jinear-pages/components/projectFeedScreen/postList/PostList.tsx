import React, { useState } from "react";
import styles from "./PostList.module.css";
import { ProjectPostDto } from "@/be/jinear-core";
import { useRetrieveProjectFeedQuery } from "@/api/projectFeedApi";
import useTranslation from "@/locals/useTranslation";
import ProjectPost from "@/components/projectPost/ProjectPost";
import PaginatedList from "@/components/paginatedList/PaginatedList";

interface PostListProps {
  projectId: string;
  workspaceName: string;
}

const PostList: React.FC<PostListProps> = ({ projectId, workspaceName }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: retrieveProjectFeedResponse, isFetching, isLoading } = useRetrieveProjectFeedQuery(
    { projectId, page });

  const renderContentItem = (data: ProjectPostDto) => {
    return <ProjectPost
      key={data.projectPostId}
      post={data}
      withCommentCountButton={true}
    />;
  };

  const totalCountTitle = retrieveProjectFeedResponse ? `(${retrieveProjectFeedResponse.data.totalElements})` : "";
  const title = `${t("projectFeedPostsTitle")} ${totalCountTitle}`;

  return (
    <div className={styles.container}>
      <PaginatedList
        id={`endless-scroll-project-feed-${projectId}`}
        data={retrieveProjectFeedResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderContentItem}
        emptyLabel={t("projectFeedPageEmptyLabel")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.contentContainerClassName}
        listTitle={title}
        paginationPosition={"bottom"}
      />
    </div>
  );
};

export default PostList;