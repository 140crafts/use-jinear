import ProfilePhoto from "@/components/profilePhoto";
import type {PlainAccountProfileDto, WorkspaceActivityDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import BasicTextDiff from "../basicTextDiff/BasicTextDiff";
import styles from "./CollaboratorsChangeDiffInfo.module.css";

interface CollaboratorsChangeDiffInfoProps {
    activity: WorkspaceActivityDto;
}

interface CollaboratorListProps {
    keyPrefix: string;
    collaborators?: PlainAccountProfileDto[] | null;
}

const CollaboratorList: React.FC<CollaboratorListProps> = ({keyPrefix, collaborators}) => {
    const {t} = useTranslation();

    if (!collaborators || collaborators.length == 0) {
        return <>{t("taskWorkflowActivityInfoAssigneeNoone")}</>;
    }

    return (
        <div className={styles.list}>
            {collaborators.map((collaborator) => (
                <div key={`${keyPrefix}-${collaborator.accountId}`} className={styles.listItem}>
                    <ProfilePhoto
                        boringAvatarKey={collaborator.accountId}
                        url={collaborator.profilePicture?.url}
                        wrapperClassName={styles.profilePic}
                    />
                    {collaborator.username}
                </div>
            ))}
        </div>
    );
};

const CollaboratorsChangeDiffInfo: React.FC<CollaboratorsChangeDiffInfoProps> = ({activity}) => {
    return (
        <BasicTextDiff
            oldState={
                <CollaboratorList
                    keyPrefix={`activity-${activity.workspaceActivityId}-old-collaborator`}
                    collaborators={activity.oldCollaborators}
                />
            }
            newState={
                <CollaboratorList
                    keyPrefix={`activity-${activity.workspaceActivityId}-new-collaborator`}
                    collaborators={activity.newCollaborators}
                />
            }
        />
    );
};

export default CollaboratorsChangeDiffInfo;
