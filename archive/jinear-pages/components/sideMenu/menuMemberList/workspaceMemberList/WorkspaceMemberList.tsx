import { PageDto, WorkspaceMemberDto } from "@/model/be/jinear-core";
import React from "react";
import MemberProfilePictureList from "../memberProfilePictureList/MemberProfilePictureList";
import styles from "./WorkspaceMemberList.module.css";

interface WorkspaceMemberListProps {
  page: PageDto<WorkspaceMemberDto>;
}

const WorkspaceMemberList: React.FC<WorkspaceMemberListProps> = ({ page }) => {
  return (
    <div className={styles.container}>
      <MemberProfilePictureList accountList={page.content.map((member) => member.account)} type="workspace" />
    </div>
  );
};

export default WorkspaceMemberList;
