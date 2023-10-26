"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface WorkspacePageProps {}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
  const router = useRouter();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  useEffect(() => {
    if (workspaceName) {
      router.replace(`/${workspaceName}/calendar`);
    }
  }, [workspaceName]);

  return (
    <div className={styles.container}>
      <CircularLoading />
    </div>
  );
};
export default WorkspacePage;
