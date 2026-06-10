import { FeedMemberDto, WorkspaceDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./FeedMenu.module.css";
import FeedTitle from "./feedTitle/FeedTitle";

interface FeedMenuProps {
  workspace: WorkspaceDto;
  feedMember: FeedMemberDto;
}

const FeedMenu: React.FC<FeedMenuProps> = ({ workspace, feedMember }) => {
  return (
    <div className={styles.container}>
      <FeedTitle workspace={workspace} feed={feedMember.feed} />
      <div className="spacer-h-1" />
      <div className={styles.feedActionButtonContainer}></div>
    </div>
  );
};

export default FeedMenu;
