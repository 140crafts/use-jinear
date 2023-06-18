import GenericBreadcrumb from "@/components/genericBreadcrumb/GenericBreadcrumb";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { useRetrieveTopicByTagQuery } from "@/store/api/topicApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
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

  const {
    data: topicResponse,
    isSuccess,
    isFetching,
  } = useRetrieveTopicByTagQuery(
    { topicTag, teamId: team?.teamId || "", workspaceId: workspace?.workspaceId || "" },
    { skip: topicTag == null || topicTag == "" || team?.teamId == null || workspace?.workspaceId == null }
  );

  return (
    <div className={styles.container}>
      <GenericBreadcrumb
        workspace={workspace}
        team={team}
        label={topicTag}
        pathAfterWorkspaceAndTeam={`topic/related-tasks/${topicTag}`}
      />{" "}
      {isFetching && (
        <div className="loadingContainer">
          <CircularProgress size={14} />
        </div>
      )}
      {team && topicResponse && isSuccess && !isFetching && (
        <MultiViewTaskList
          title={t("topicTaskListName").replace("${topicTag}", topicTag)}
          workspaceId={team.workspaceId}
          teamId={team.teamId}
          topicIds={[topicResponse.data.topicId]}
          activeDisplayFormat="LIST"
        />
      )}
    </div>
  );
};

export default TopicTaskListScreen;
