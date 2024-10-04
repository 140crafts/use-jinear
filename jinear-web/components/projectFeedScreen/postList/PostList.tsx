import React, { useEffect, useState } from "react";
import styles from "./PostList.module.css";
import { ProjectPostDto } from "@/be/jinear-core";
import { useRetrieveProjectFeedQuery } from "@/api/projectFeedApi";
import EndlessScrollList from "@/components/endlessScrollList/EndlessScrollList";
import useTranslation from "@/locals/useTranslation";
import ProjectPost from "@/components/projectPost/ProjectPost";
import ProjectsTitle from "@/components/workspaceProjectsScreen/projectsTitle/ProjectsTitle";
import PaginatedList from "@/components/paginatedList/PaginatedList";

interface PostListProps {
  projectId: string;
  accessKey: string;
}

const PostList: React.FC<PostListProps> = ({ projectId, accessKey }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: retrieveProjectFeedResponse, isFetching, isLoading } = useRetrieveProjectFeedQuery(
    { projectId, page });

  const renderContentItem = (data: ProjectPostDto) => {
    return <ProjectPost key={data.projectPostId} post={data} accessKey={accessKey} />;
  };

  const loadMore = () => {
    setPage(page + 1);
  };

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
      />
    </div>
  );
};

export default PostList;