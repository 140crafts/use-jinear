import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import type {TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./TeamTopicSettings.module.scss";

interface TeamTopicSettingsProps {
    workspace: WorkspaceDto;
    team: TeamDto;
}

const TeamTopicSettings: React.FC<TeamTopicSettingsProps> = ({workspace, team}) => {
    const {t} = useTranslation();
    const topicRoot = `/${workspace.username}/tasks/${encodeURIComponent(team.username)}/topic`;

    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("teamSettingsScreenTopicSectionTitle")}
                description={t("teamSettingsScreenTopicSectionDescription")}
            />
            <div className={styles.actionButtonContainer}>
                <Button
                    variant={ButtonVariants.outline}
                    heightVariant={ButtonHeight.short}
                    href={`${topicRoot}/list`}
                >
                    {t("teamSettingsScreenTopicSectionListTopicsButton")}
                </Button>
                {team.teamState == "ACTIVE" && (
                    <Button
                        variant={ButtonVariants.filled}
                        heightVariant={ButtonHeight.short}
                        href={`${topicRoot}/new`}
                    >
                        {t("teamSettingsScreenTopicSectionNewTopicButton")}
                    </Button>
                )}
            </div>
        </div>
    );
};

export default TeamTopicSettings;
