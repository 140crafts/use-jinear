import TopicForm from "@/components/form/topicForm/TopicForm";
import NewTopicScreenBreadcrumb from "@/components/topicScreen/newTopicScreen/newTopicScreenBreadcrumb/NewTopicScreenBreadcrumb";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface NewTopicPageProps {}

const NewTopicPage: React.FC<NewTopicPageProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  return (
    <div className={styles.container}>
      {workspace && team && <NewTopicScreenBreadcrumb workspace={workspace} team={team} />}
      <div className="spacer-h-4" />
      <h1>{t("newTopicScreenTitle")}</h1>
      <div className={styles.formContainer}>{workspace && team && <TopicForm workspace={workspace} team={team} />}</div>
    </div>
  );
};

export default NewTopicPage;
