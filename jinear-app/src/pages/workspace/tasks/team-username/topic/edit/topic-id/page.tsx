import TopicForm from "@/components/form/topicForm/TopicForm";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {useRetrieveTopicQuery} from "@/store/api/topicApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import {useParams} from "react-router-dom";

interface EditTopicScreenProps {
}

const EditTopicScreen: React.FC<EditTopicScreenProps> = ({}) => {
    const {t} = useTranslation();
    const params = useParams();
    const topicId: string = params?.topicId as string;
    const workspaceName: string = params?.workspaceName as string;
    const teamUsername: string = params?.teamUsername as string;

    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    const {data: teamsResponse} = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {skip: workspace == null});
    const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

    const {
        data: topicResponse,
        isLoading: isTopicResponseLoading,
        isSuccess: isTopicResponseSuccess,
    } = useRetrieveTopicQuery(topicId, {skip: topicId == null});

    return (
        <div className={styles.container}>
            <div className="spacer-h-4"/>
            <SectionTitle
                title={t("topicEditScreenTitle")}
                description={t("topicEditScreenDescription")}
            />
            {isTopicResponseLoading && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            )}

            {!isTopicResponseLoading && isTopicResponseSuccess && workspace && team && (
                <div className={styles.formContainer}>
                    <TopicForm
                        workspace={workspace}
                        team={team}
                        topicId={topicResponse?.data.topicId}
                        color={topicResponse?.data.color}
                        taskName={topicResponse?.data.name}
                        taskTag={topicResponse?.data.tag}
                    />
                </div>
            )}
        </div>
    );
};

export default EditTopicScreen;
