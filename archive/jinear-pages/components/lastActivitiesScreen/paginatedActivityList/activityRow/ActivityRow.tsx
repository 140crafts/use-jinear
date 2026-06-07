import Button, { ButtonVariants } from "@/components/button";
import TaskActivity from "@/components/taskDetail/taskActivity/TaskActivity";
import useWindowSize from "@/hooks/useWindowSize";
import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import { popTaskOverviewModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import { IoArrowForward } from "react-icons/io5";
import styles from "./ActivityRow.module.css";

interface ActivityRowProps {
  activity: WorkspaceActivityDto;
  sameGroup: boolean | null;
  ignoreGrouping?: boolean;
  index?: number;
}

const ActivityRow: React.FC<ActivityRowProps> = ({ index = 0, sameGroup, ignoreGrouping, activity }) => {
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
        <>
          {index != 0 && <div className="spacer-h-2" />}
          <Button
            className={styles.titleContainer}
            variant={ButtonVariants.filled}
            href={activity.groupLink || undefined}
            onClick={onLinkClick}
          >
            <b className={styles.groupTitle}>{activity.groupTitle}</b>
            <div className="flex-1" />
            <div className={styles.iconContainer}>
              <IoArrowForward />
            </div>
          </Button>
        </>
      )}
      <TaskActivity activity={activity} />
    </div>
  );
};

export default ActivityRow;
