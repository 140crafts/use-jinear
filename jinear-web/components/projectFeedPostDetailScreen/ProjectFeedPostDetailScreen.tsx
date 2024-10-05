import React from "react";
import styles from "./ProjectFeedPostDetailScreen.module.css";
import ProjectPost from "@/components/projectPost/ProjectPost";
import { useRetrieveProjectFeedPostQuery } from "@/api/projectFeedApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Button, { ButtonHeight } from "@/components/button";
import { IoArrowBack } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";

interface ProjectFeedPostDetailScreenProps {
  projectId: string;
  postId: string;
  accessKey: string;
}

const ProjectFeedPostDetailScreen: React.FC<ProjectFeedPostDetailScreenProps> = ({ projectId, postId, accessKey }) => {
  const { data: projectFeedPostQueryResponse, isFetching } = useRetrieveProjectFeedPostQuery({ projectId, postId });
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      {isFetching && <CircularLoading />}
      {projectFeedPostQueryResponse?.data &&
        <>
          <Button
            heightVariant={ButtonHeight.short}
            href={`/project-feed/${accessKey}`}
            className={styles.goBackButton}
          >
            <b><IoArrowBack /></b>
            {t("projectPostGoBackToFeed")}
          </Button>
          <ProjectPost post={projectFeedPostQueryResponse.data} accessKey={accessKey} asLink={false} />
        </>
      }
    </div>
  );
};

export default ProjectFeedPostDetailScreen;