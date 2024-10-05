import React from "react";
import styles from "./ProjectFeedScreen.module.css";
import useTranslation from "@/locals/useTranslation";
import PostList from "@/components/projectFeedScreen/postList/PostList";
import NewPostInput from "@/components/projectFeedScreen/newPostInput/NewPostInput";
import { useTypedSelector } from "@/store/store";
import { selectAuthState, selectCurrentAccountId } from "@/slice/accountSlice";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveProjectPermissionsQuery, useRetrieveProjectQuery } from "@/api/projectQueryApi";
import ProjectInfo from "@/components/projectFeedScreen/projectInfo/ProjectInfo";

interface ProjectFeedScreenProps {
  accessKey: string;
}

const ProjectFeedScreen: React.FC<ProjectFeedScreenProps> = ({ accessKey }) => {
  const { t } = useTranslation();
  const authState = useTypedSelector(selectAuthState);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const projectId = accessKey?.substring(accessKey?.length - 26, accessKey.length);

  const {
    data: accountProjectPermissions,
    isFetching: isAccountProjectPermissionsFetching
  } = useRetrieveProjectPermissionsQuery({ projectId }, { skip: currentAccountId == null });

  const {
    data: retrieveProjectResponse,
    isFetching: isRetrieveProjectFetching
  } = useRetrieveProjectQuery({ projectId });


  return (authState == "NOT_DECIDED" || isAccountProjectPermissionsFetching || isRetrieveProjectFetching) ?
    <CircularLoading /> : (
      <div className={styles.container}>
        {retrieveProjectResponse && <ProjectInfo publicProject={retrieveProjectResponse.data} />}
        {(accountProjectPermissions?.data.canInitializePost) && <NewPostInput projectId={projectId} />}
        <PostList projectId={projectId} accessKey={accessKey} />
      </div>
    );
};

export default ProjectFeedScreen;