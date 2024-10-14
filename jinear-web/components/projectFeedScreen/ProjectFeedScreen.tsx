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
import { useRetrievePublicProjectInfoQuery } from "@/api/projectFeedApi";

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
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoQuery({ projectId });


  return (authState == "NOT_DECIDED" || isAccountProjectPermissionsFetching || isRetrievePublicProjectFetching) ?
    <CircularLoading /> : (
      <div className={styles.container}>
        {retrievePublicProjectResponse && <ProjectInfo publicProject={retrievePublicProjectResponse.data} />}
        {(retrievePublicProjectResponse && accountProjectPermissions?.data.canInitializePost) &&
          <NewPostInput projectId={projectId} workspaceId={retrievePublicProjectResponse.data.workspaceId} />}
        <PostList projectId={projectId} accessKey={accessKey} />
      </div>
    );
};

export default ProjectFeedScreen;