import Button, { ButtonVariants } from "@/components/button";
import TopicCard from "@/components/topicScreen/topicListScreen/topicCard/TopicCard";
import TopicListScreenBreadcrumb from "@/components/topicScreen/topicListScreen/topicListScreenBreadcrumb/TopicListScreenBreadcrumb";
import Transition from "@/components/transition/Transition";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface TeamTopicListScreenProps {}

const TeamTopicListScreen: React.FC<TeamTopicListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const {
    data: teamTopicListingResponse,
    isSuccess,
    isError,
    isLoading,
  } = useRetrieveTeamTopicsQuery(preferredTeam?.teamId || "", {
    skip: preferredTeam?.teamId == null,
  });

  return (
    <div className={styles.container}>
      {!preferredWorkspace?.isPersonal && <TopicListScreenBreadcrumb />}
      <div className="spacer-h-4" />
      <h1>{t("topicListScreenTitle")}</h1>
      <div className="spacer-h-4" />

      {isLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}

      {!isLoading && isSuccess && preferredWorkspace && preferredTeam && (
        <Transition className={styles.content} initial={true}>
          {teamTopicListingResponse.data.content.map((topicDto) => (
            <TopicCard
              key={topicDto.topicId}
              topic={topicDto}
              workspaceName={preferredWorkspace.username}
              teamUsername={preferredTeam.username}
            />
          ))}
          {!teamTopicListingResponse?.data.hasContent && (
            <div className={styles.emptyStateContainer}>
              <div>{t("topicListScreenNoContentLabel")}</div>
              <Button
                variant={ButtonVariants.filled}
                href={`/${preferredWorkspace.username}/${encodeURI(preferredTeam.username)}/topic/new`}
              >
                {t("topicListScreenNoContentNewTopicLabel")}
              </Button>
            </div>
          )}
        </Transition>
      )}
    </div>
  );
};

export default TeamTopicListScreen;
