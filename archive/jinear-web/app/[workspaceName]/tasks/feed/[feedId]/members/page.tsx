"use client";
import FeedMemberList from "@/components/feedMembersPage/feedMemberList/FeedMemberList";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface MembersPageProps {}

const MembersPage: React.FC<MembersPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const feedId: string = params?.feedId as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return (
    <div className={styles.container}>{workspace && <FeedMemberList feedId={feedId} workspaceId={workspace.workspaceId} />}</div>
  );
};

export default MembersPage;
