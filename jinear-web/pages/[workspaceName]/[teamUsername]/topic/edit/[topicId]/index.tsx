import TopicForm from "@/components/form/topicForm/TopicForm";
import EditTopicBreadCrumb from "@/components/topicScreen/topicEditScreen/editTopicBreadCrumb/EditTopicBreadCrumb";
import Transition from "@/components/transition/Transition";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { useRetrieveTopicQuery } from "@/store/api/topicApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface EditTopicScreenProps {}

const EditTopicScreen: React.FC<EditTopicScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const topicId: string = router.query?.topicId as string;
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", { skip: workspace == null });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  const {
    data: topicResponse,
    isLoading: isTopicResponseLoading,
    isSuccess: isTopicResponseSuccess,
  } = useRetrieveTopicQuery(topicId, { skip: topicId == null });

  return (
    <div className={styles.container}>
      {workspace && team && topicResponse && (
        <EditTopicBreadCrumb workspace={workspace} team={team} topicName={topicResponse.data.name} topicId={topicId} />
      )}
      <div className="spacer-h-4" />
      <h1>{t("topicEditScreenTitle")}</h1>
      {isTopicResponseLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}

      {!isTopicResponseLoading && isTopicResponseSuccess && (
        <Transition initial={true} className={styles.formContainer}>
          <TopicForm
            workspaceId={workspace?.workspaceId}
            teamId={team?.teamId}
            topicId={topicResponse?.data.topicId}
            color={topicResponse?.data.color}
            taskName={topicResponse?.data.name}
            taskTag={topicResponse?.data.tag}
          />
        </Transition>
      )}
    </div>
  );
};

export default EditTopicScreen;
