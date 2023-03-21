import PaginatedTopicTaskList from "@/components/taskListScreen/taskLists/paginatedTopicTaskList/PaginatedTopicTaskList";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TopicTaskListScreenProps {}

const TopicTaskListScreen: React.FC<TopicTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const topicTag: string = router.query?.topicTag as string;

  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      {team && workspace && topicTag && (
        <PaginatedTopicTaskList teamId={team.teamId} workspaceId={workspace.workspaceId} topicTag={topicTag} />
      )}
    </div>
  );
};

export default TopicTaskListScreen;
