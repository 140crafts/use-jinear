import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import type {TeamMemberDto} from "@/model/be/jinear-core";
import {useRetrieveTeamMembersQuery} from "@/store/api/teamMemberApi";
import {useUpdateTaskCollaboratorsMutation} from "@/store/api/taskUpdateApi";
import {popTeamMemberPickerModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {useTask} from "../../context/TaskDetailContext";
import styles from "./ChangeCollaboratorsButton.module.css";

interface ChangeCollaboratorsButtonProps {
    className?: string;
}

const VISIBLE_PROFILE_PHOTO_COUNT = 3;

const ChangeCollaboratorsButton: React.FC<ChangeCollaboratorsButtonProps> = ({className}) => {
    const task = useTask();
    const {t} = useTranslation();
    const dispatch = useAppDispatch();

    const [updateTaskCollaborators, {isLoading}] = useUpdateTaskCollaboratorsMutation();

    const collaborators = task.taskCollaborators ?? [];
    const hasCollaborators = collaborators.length != 0;

    const {data: teamMemberListResponse} = useRetrieveTeamMembersQuery({teamId: task.teamId});
    const selectedTeamMembers = teamMemberListResponse?.data.content.filter(
        (teamMemberDto) => collaborators.findIndex((collaborator) => collaborator.accountId == teamMemberDto.accountId) != -1
    );

    const onPick = (pickedList: TeamMemberDto[]) => {
        updateTaskCollaborators({
            taskId: task.taskId,
            body: {collaboratorIds: pickedList.map((teamMemberDto) => teamMemberDto.accountId)}
        });
    };

    const popCollaboratorPicker = () => {
        dispatch(
            popTeamMemberPickerModal({
                visible: true,
                multiple: true,
                teamId: task.teamId,
                initialSelectionOnMultiple: selectedTeamMembers,
                onPick
            })
        );
    };

    return (
        <Button
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.short}
            className={className}
            onClick={popCollaboratorPicker}
            loading={isLoading}
        >
            {hasCollaborators ? (
                <>
                    <div>{t("taskDetailCollaborators")}</div>
                    <div className={styles.profilePicContainer}>
                        {collaborators.slice(0, VISIBLE_PROFILE_PHOTO_COUNT).map((collaborator) => (
                            <ProfilePhoto
                                key={`task-collaborator-${collaborator.taskCollaboratorId}`}
                                boringAvatarKey={collaborator.accountId}
                                url={collaborator.collaborator.profilePicture?.url}
                                wrapperClassName={styles.profilePic}
                            />
                        ))}
                    </div>
                    <b>
                        {collaborators.length == 1
                            ? collaborators[0].collaborator.username
                            : `${collaborators.length}`}
                    </b>
                </>
            ) : (
                <>{t("taskDetailAddCollaborators")}</>
            )}
        </Button>
    );
};

export default ChangeCollaboratorsButton;
