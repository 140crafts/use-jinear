"use client";
import LastActivitiesList from "@/components/lastActivitiesScreen/LastActivitiesList";
import LastActivitiesBreadCrumb from "@/components/lastActivitiesScreen/lastActivitiesScreenHeader/breadcrumb/LastActivitiesBreadCrumb";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface LastActivitiesScreenProps {}

const LastActivitiesScreen: React.FC<LastActivitiesScreenProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return (
    <div className={styles.container}>
      {workspace && <LastActivitiesBreadCrumb workspace={workspace} />}
      {workspace && <LastActivitiesList workspace={workspace} />}
    </div>
  );
};

export default LastActivitiesScreen;
