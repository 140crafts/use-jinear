"use client";
import React, { useEffect } from "react";
import styles from "./index.module.scss";
import { useParams } from "next/navigation";
import { useRetrieveProjectQuery } from "@/api/projectQueryApi";
import ProjectDetailScreen from "@/components/projectDetailScreen/ProjectDetailScreen";
import { useAppDispatch } from "@/store/store";
import { changeLoadingModalVisibility } from "@/slice/modalSlice";

interface ProjectDetailPageProps {

}

const ProjectDetailPage: React.FC<ProjectDetailPageProps> = ({}) => {
  const params = useParams();
  const dispatch = useAppDispatch();
  const workspaceName: string = params?.workspaceName as string;
  const projectId: string = params?.projectId as string;

  const {
    data: retrieveProjectResponse,
    isFetching,
    isSuccess
  } = useRetrieveProjectQuery({ projectId }, { skip: projectId == null });

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isFetching }));
  }, [dispatch, isFetching]);

  return (
    <div className={styles.container}>
      {isSuccess && retrieveProjectResponse.data &&
        <ProjectDetailScreen project={retrieveProjectResponse.data} isFetching={isFetching} />}
    </div>
  );
};

export default ProjectDetailPage;