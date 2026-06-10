"use client";
import React from "react";
import styles from "./ProjectFeedScreen.module.css";
import useTranslation from "@/locals/useTranslation";
import PostList from "@/components/projectFeedScreen/postList/PostList";
import NewPostInput from "@/components/projectFeedScreen/newPostInput/NewPostInput";
import { useTypedSelector } from "@/store/store";
import { selectAuthState, selectCurrentAccountId } from "@/slice/accountSlice";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveProjectPermissionsQuery } from "@/api/projectQueryApi";
import ProjectInfo from "@/components/projectFeedScreen/projectInfo/ProjectInfo";
import { useRetrievePublicProjectInfoWithDomainQuery } from "@/api/projectFeedApi";

interface ProjectFeedScreenProps {
}

const ProjectFeedScreen: React.FC<ProjectFeedScreenProps> = ({}) => {
  const { t } = useTranslation();
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const domain = typeof window == "object" && window?.location?.hostname || "";

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoWithDomainQuery({ domain }, { skip: domain == "" });

  const projectId = retrievePublicProjectResponse?.data?.projectId || "";

  const {
    data: accountProjectPermissions,
    isFetching: isAccountProjectPermissionsFetching
  } = useRetrieveProjectPermissionsQuery({ projectId }, { skip: currentAccountId == null || projectId == "" });


  return (authState == "NOT_DECIDED" || isAccountProjectPermissionsFetching || isRetrievePublicProjectFetching) ?
    <CircularLoading /> : (
      <>
        <div className={styles.container}>
          {retrievePublicProjectResponse && <ProjectInfo publicProject={retrievePublicProjectResponse.data} />}
          {(retrievePublicProjectResponse && accountProjectPermissions?.data.canInitializePost) &&
            <NewPostInput projectId={projectId} workspaceId={retrievePublicProjectResponse.data.workspaceId} />}
          {retrievePublicProjectResponse &&
            <PostList projectId={projectId} workspaceName={retrievePublicProjectResponse.data.workspaceUsername} />
          }
        </div>
      </>
    );
};

export default ProjectFeedScreen;