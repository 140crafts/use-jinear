import React, { useEffect } from "react";
import styles from "./ProjectPost.module.css";
import { ProjectPostDto } from "@/be/jinear-core";
import ProfilePhoto from "@/components/profilePhoto";
import Tiptap from "@/components/tiptap/Tiptap";
import PostFile from "@/components/projectPost/postFile/PostFile";
import Line from "@/components/line/Line";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import Link from "next/link";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import Button, { ButtonHeight } from "@/components/button";
import { LuMessageCircle, LuTrash } from "react-icons/lu";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import { useRetrieveProjectPermissionsQuery } from "@/api/projectQueryApi";
import { closeDialogModal, popDialogModal } from "@/slice/modalSlice";
import { useDeleteProjectFeedPostMutation } from "@/api/projectPostApi";
import { useRouter } from "next/navigation";
import { PROJECT_FEED_URL } from "@/utils/constants";

interface ProjectPostProps {
  post: ProjectPostDto;
  workspaceName: string;
  accessKey: string;
  asLink?: boolean;
  withCommentCountButton?: boolean;
  withSeperator?: boolean;
}

const ProjectPost: React.FC<ProjectPostProps> = ({
                                                   post,
                                                   workspaceName,
                                                   accessKey,
                                                   asLink = true,
                                                   withCommentCountButton = true,
                                                   withSeperator = true
                                                 }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();

  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const { data: projectPermissionsResponse } = useRetrieveProjectPermissionsQuery({ projectId: post.projectId }, { skip: currentAccountId == null });

  const canDelete = post.accountId == currentAccountId || projectPermissionsResponse?.data?.accountIsProjectTeamsAdmin || projectPermissionsResponse?.data?.accountWorkspaceAdminOrOwner;
  const [deletePostCall, { data: deletePostResponse }] = useDeleteProjectFeedPostMutation();

  useEffect(() => {
    if (deletePostResponse && accessKey) {
      router.replace(`/project-feed/${accessKey}`);
    }
  }, [router, accessKey, deletePostResponse]);

  const popAreYouSureToDeletePostModal = (e: Event) => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("projectPostDeleteAreYouSureTitle"),
        content: t("projectPostDeleteAreYouSureText"),
        confirmButtonLabel: t("projectPostDeleteAreYouSureConfirmLabel"),
        onConfirm: deletePost
      })
    );
  };

  const deletePost = () => {
    deletePostCall({ projectId: post.projectId, postId: post.projectPostId });
    dispatch(closeDialogModal());
  };

  const Wrapper = asLink ? Link : ClientOnly;
  const projectUrl = PROJECT_FEED_URL.replace("[accessKey]", accessKey).replace("[workspaceUsername]", workspaceName);
  const href = asLink ? `${projectUrl}/post/${post.projectPostId}` : undefined;

  return (
    <div className={styles.container}>
      <div className={styles.contentContainer}>
        <div className={styles.profilePicContainer}>
          <ProfilePhoto
            boringAvatarKey={post.account?.accountId || ""}
            storagePath={post.account?.profilePicture?.storagePath}
            wrapperClassName={styles.profilePic}
          />
        </div>
        <div className={styles.content}>
          <Wrapper
            // @ts-ignore .
            href={href}>
            <Tiptap
              className={styles.input}
              editorClassName={styles.input}
              editorWrapperClassName={styles.editorWrapper}
              editable={false}
              hideActionBarWhenEmpty={false}
              content={post.postBody.value}
            />
          </Wrapper>
          <div className={styles.attachments}>
            {post.files?.map(file => <PostFile key={file.mediaId} file={file} />)}
          </div>
        </div>
      </div>

      <div className={styles.postInfo}>
        {canDelete &&
          <Button
            heightVariant={ButtonHeight.short}
            className={styles.deleteButton}
            onClick={popAreYouSureToDeletePostModal}>
            <LuTrash className={"icon"} />
            {t("projectPostDeleteButton")}
          </Button>}
        {withCommentCountButton &&
          <Button
            className={styles.commentButton}
            heightVariant={ButtonHeight.short}
            href={href}>
            <LuMessageCircle className={"icon"} />
            {t("projectPostCommentCountLabel").replace("${number}", `${post?.commentCount}`)}
          </Button>}
        <span>
          {format(new Date(post.createdDate), t("dateFormatShortMonthReadable"))}
        </span>
        <b>{post.account?.username || ""}</b>
      </div>
      {withSeperator && <Line />}
    </div>

  );
};

export default ProjectPost;