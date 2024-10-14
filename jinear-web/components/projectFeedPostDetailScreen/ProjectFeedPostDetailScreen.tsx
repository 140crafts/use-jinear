import React, { Dispatch, SetStateAction, useState } from "react";
import styles from "./ProjectFeedPostDetailScreen.module.css";
import ProjectPost from "@/components/projectPost/ProjectPost";
import { useRetrieveProjectFeedPostQuery, useRetrievePublicProjectInfoQuery } from "@/api/projectFeedApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Button, { ButtonHeight } from "@/components/button";
import { IoArrowBack } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";
import PostCommentList from "@/components/projectFeedPostDetailScreen/postCommentList/PostCommentList";
import CommentInput from "@/components/projectFeedPostDetailScreen/commentInput/CommentInput";
import { ProjectPostCommentDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";

interface ProjectFeedPostDetailScreenProps {
  projectId: string;
  postId: string;
  accessKey: string;
}

const logger = Logger("ProjectFeedPostDetailScreen");
const ProjectFeedPostDetailScreen: React.FC<ProjectFeedPostDetailScreenProps> = ({ projectId, postId, accessKey }) => {
  const { t } = useTranslation();
  const { data: projectFeedPostQueryResponse, isFetching } = useRetrieveProjectFeedPostQuery({ projectId, postId });
  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoQuery({ projectId });
  const canComment = retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.canComment ?? false;
  const hasExplicitAdminDeleteAccess = (retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.accountWorkspaceAdminOrOwner ?? false) || (retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.accountIsProjectTeamsAdmin ?? false);
  const [quotedComment, setQuotedComment] = useState<ProjectPostCommentDto>();

  logger.log({ quotedComment });

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
          <ProjectPost
            post={projectFeedPostQueryResponse.data}
            accessKey={accessKey}
            asLink={false}
            withCommentCountButton={false}
            withSeperator={false}
          />

          <PostCommentList
            postId={postId}
            projectId={projectId}
            setQuotedComment={setQuotedComment}
            canComment={canComment}
            hasExplicitAdminDeleteAccess={hasExplicitAdminDeleteAccess}
          />

          {canComment &&
            <CommentInput
              postId={postId}
              projectId={projectId}
              quotedComment={quotedComment}
              setQuotedComment={setQuotedComment}
            />
          }
        </>
      }
    </div>
  );
};

export default ProjectFeedPostDetailScreen;