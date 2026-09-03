import Button, {ButtonVariants} from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import TopicCard from "@/components/topicScreen/topicListScreen/topicCard/TopicCard";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {useRetrieveTeamTopicsQuery} from "@/store/api/topicListingApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TeamTopicListScreenProps {
}

const TeamTopicListScreen: React.FC<TeamTopicListScreenProps> = ({}) => {
    const {t} = useTranslation();
    const params = useParams();
    const workspaceName: string = params?.workspaceName as string;
    const teamUsername: string = params?.teamUsername as string;

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {
        data: teamsResponse,
        isFetching: isTeamsFetching
    } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
        skip: workspace == null,
    });
    const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

    const {
        currentData: teamTopicListingResponse,
        isFetching,
    } = useRetrieveTeamTopicsQuery(team?.teamId || "", {
        skip: team == null,
    });

    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("topicListScreenTitle")}
                description={t("topicListScreenDescription")}
            />
            <div className="spacer-h-4"/>

            {isFetching && teamTopicListingResponse == null && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            )}

            {teamTopicListingResponse && workspace && team && (
                <div className={styles.content}>
                    {teamTopicListingResponse.data.content.map((topicDto) => (
                        <TopicCard key={topicDto.topicId} topic={topicDto} workspaceName={workspace.username}
                                   teamUsername={team.username}/>
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
                </div>
            )}
        </div>
    );
};

export default TeamTopicListScreen;
