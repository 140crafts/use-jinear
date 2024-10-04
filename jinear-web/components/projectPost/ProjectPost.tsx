import React, { useEffect } from "react";
import styles from "./ProjectPost.module.scss";
import { ProjectPostDto } from "@/be/jinear-core";
import ProfilePhoto from "@/components/profilePhoto";
import Tiptap from "@/components/tiptap/Tiptap";
import PostFile from "@/components/projectPost/postFile/PostFile";
import Line from "@/components/line/Line";
import { format } from "date-fns";
import useTranslation from "@/locals/useTranslation";
import Link from "next/link";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import Button from "@/components/button";
import { LuTrash } from "react-icons/lu";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import { useRetrieveProjectPermissionsQuery } from "@/api/projectQueryApi";
import { closeDialogModal, popDialogModal } from "@/slice/modalSlice";
import { useDeleteProjectFeedPostMutation } from "@/api/projectPostApi";
import { useRouter } from "next/navigation";

interface ProjectPostProps {
  post: ProjectPostDto;
  accessKey: string;
  asLink?: boolean;
}

const ProjectPost: React.FC<ProjectPostProps> = ({ post, accessKey, asLink = true }) => {
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
  const href = asLink ? `/project-feed/${accessKey}/post/${post.projectPostId}` : undefined;

  return (
    <div className={styles.baseContainer}>
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
              // @ts-ignore
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
          <b>
            {format(new Date(post.createdDate), t("dateFormatShortMonthReadable"))}
          </b>
          {canDelete && <Button className={styles.deleteButton} onClick={popAreYouSureToDeletePostModal}>
            <LuTrash className={"icon"} />
          </Button>}
        </div>
        <Line />
      </div>
    </div>
  );
};

export default ProjectPost;