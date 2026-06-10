"use client";
import Button, { ButtonVariants } from "@/components/button";
import TopicCard from "@/components/topicScreen/topicListScreen/topicCard/TopicCard";
import Transition from "@/components/transition/Transition";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface TeamTopicListScreenProps {}

const TeamTopicListScreen: React.FC<TeamTopicListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  const {
    data: teamTopicListingResponse,
    isSuccess,
    isLoading,
  } = useRetrieveTeamTopicsQuery(team?.teamId || "", {
    skip: team == null,
  });

  return (
    <div className={styles.container}>
      <h1>{t("topicListScreenTitle")}</h1>
      <div className="spacer-h-4" />

      {isLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}

      {!isLoading && isSuccess && workspace && team && (
        <Transition className={styles.content} initial={true}>
          {teamTopicListingResponse.data.content.map((topicDto) => (
            <TopicCard key={topicDto.topicId} topic={topicDto} workspaceName={workspace.username} teamUsername={team.username} />
          ))}
          {!teamTopicListingResponse?.data.hasContent && (
            <div className={styles.emptyStateContainer}>
              <div>{t("topicListScreenNoContentLabel")}</div>
              <Button
                variant={ButtonVariants.filled}
                href={`/${workspace.username}/tasks/${encodeURIComponent(team.username)}/topic/new`}
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
