import TopicForm from "@/components/form/topicForm/TopicForm";
import NewTopicScreenBreadcrumb from "@/components/topicScreen/newTopicScreen/newTopicScreenBreadcrumb/NewTopicScreenBreadcrumb";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface NewTopicPageProps {}

const NewTopicPage: React.FC<NewTopicPageProps> = ({}) => {
  const { t } = useTranslation();

  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <NewTopicScreenBreadcrumb />
      <div className="spacer-h-4" />
      <h1>{t("newTopicScreenTitle")}</h1>
      <div className={styles.formContainer}>
        <TopicForm workspaceId={workspace?.workspaceId} teamId={team?.teamId} />
      </div>
    </div>
  );
};

export default NewTopicPage;
