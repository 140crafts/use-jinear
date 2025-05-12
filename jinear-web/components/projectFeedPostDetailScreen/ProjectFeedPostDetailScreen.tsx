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
import { PROJECT_FEED_URL } from "@/utils/constants";

interface ProjectFeedPostDetailScreenProps {
  projectId: string;
  postId: string;
  accessKey: string;
  workspaceName: string;
}

const logger = Logger("ProjectFeedPostDetailScreen");
const ProjectFeedPostDetailScreen: React.FC<ProjectFeedPostDetailScreenProps> = ({
                                                                                   projectId,
                                                                                   postId,
                                                                                   accessKey,
                                                                                   workspaceName
                                                                                 }) => {
  const { t } = useTranslation();
  const { data: projectFeedPostQueryResponse, isFetching } = useRetrieveProjectFeedPostQuery({ projectId, postId });
  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoQuery({ projectId });
  const canComment = retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.canComment ?? false;
  const hasExplicitAdminDeleteAccess = (retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.accountWorkspaceAdminOrOwner ?? false) || (retrievePublicProjectResponse?.data?.accountProjectPermissionFlags?.accountIsProjectTeamsAdmin ?? false);
  const [quotedComment, setQuotedComment] = useState<ProjectPostCommentDto>();

  const projectUrl = PROJECT_FEED_URL.replace("[accessKey]", accessKey).replace("[workspaceUsername]", workspaceName);

  logger.log({ quotedComment });

  return (
    <div className={styles.container}>
      {isFetching && <CircularLoading />}
      {projectFeedPostQueryResponse?.data &&
        <>
          <Button
            heightVariant={ButtonHeight.short}
            href={projectUrl}
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
            workspaceName={workspaceName}
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