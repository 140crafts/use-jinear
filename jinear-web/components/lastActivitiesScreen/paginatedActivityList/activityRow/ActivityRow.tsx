import Button from "@/components/button";
import TaskActivity from "@/components/taskDetail/taskActivityList/taskActivity/TaskActivity";
import useWindowSize from "@/hooks/useWindowSize";
import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import styles from "./ActivityRow.module.css";

interface ActivityRowProps {
  activity: WorkspaceActivityDto;
  sameGroup: boolean | null;
  ignoreGrouping?: boolean;
}

const ActivityRow: React.FC<ActivityRowProps> = ({ sameGroup, ignoreGrouping, activity }) => {
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (!isMobile && activity.relatedTask) {
      event?.preventDefault();
      openTaskOverviewModal();
    }
  };

  const openTaskOverviewModal = () => {
    const workspaceName = activity.relatedTask?.workspace?.username;
    const taskTag = `${activity.relatedTask?.team?.tag}-${activity.relatedTask?.teamTagNo}`;
    dispatch(popTaskOverviewModal({ taskTag, workspaceName, visible: true }));
  };

  return (
    <div className={styles.container}>
      {!sameGroup && !ignoreGrouping && activity.groupTitle && (
        <Button className={styles.titleContainer} href={activity.groupLink || undefined} onClick={onLinkClick}>
          <b className={styles.groupTitle}>{activity.groupTitle}</b>
        </Button>
      )}
      <TaskActivity activity={activity} />
    </div>
  );
};

export default ActivityRow;
