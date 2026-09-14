import TopicForm from "@/components/form/topicForm/TopicForm";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface NewTopicPageProps {
}

const NewTopicPage: React.FC<NewTopicPageProps> = ({}) => {
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

    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("newTopicScreenTitle")}
                description={t("newTopicScreenDescription")}
            />
            <div className={styles.formContainer}>{workspace && team &&
                <TopicForm workspace={workspace} team={team}/>}</div>
        </div>
    );
};

export default NewTopicPage;
