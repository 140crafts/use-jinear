import ProfilePhoto from "@/components/profilePhoto";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import type {TaskInitializeRequest, TeamMemberDto} from "@/model/be/jinear-core";
import {popTeamMemberPickerModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React, {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import type {UseFormSetValue} from "react-hook-form";
import styles from "./CollaboratorPickerButton.module.css";

interface CollaboratorPickerButtonProps {
    teamId: string;
    setValue: UseFormSetValue<TaskInitializeRequest>;
}

const logger = Logger("CollaboratorPickerButton");
const VISIBLE_PROFILE_PHOTO_COUNT = 3;

export interface ICollaboratorPickerButtonRef {
    reset: () => void;
}

const CollaboratorPickerButton = ({teamId, setValue}: CollaboratorPickerButtonProps, ref: any) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [selectedCollaborators, setSelectedCollaborators] = useState<TeamMemberDto[]>([]);

    useEffect(() => {
        logger.log({selectedCollaborators});
        const collaboratorIds = selectedCollaborators.map((teamMemberDto) => teamMemberDto.accountId);
        setValue("collaboratorIds", collaboratorIds.length != 0 ? collaboratorIds : undefined);
    }, [selectedCollaborators]);

    useImperativeHandle(ref, () => ({
        reset: () => setSelectedCollaborators([])
    }));

    const onModalPick = (selection: TeamMemberDto[]) => {
        setSelectedCollaborators(selection);
    };

    const popCollaboratorPicker = () => {
        dispatch(
            popTeamMemberPickerModal({
                visible: true,
                multiple: true,
                teamId,
                initialSelectionOnMultiple: selectedCollaborators,
                onPick: onModalPick
            })
        );
    };

    const deselect = () => {
        setSelectedCollaborators([]);
    };

    return (
        <div className={styles.container}>
            <SelectDeselectButton
                hasSelection={selectedCollaborators.length != 0}
                onPickClick={popCollaboratorPicker}
                selectedComponent={
                    <div className={styles.selectedContainer}>
                        <div className={styles.profilePicContainer}>
                            {selectedCollaborators.slice(0, VISIBLE_PROFILE_PHOTO_COUNT).map((teamMemberDto) => (
                                <ProfilePhoto
                                    key={`new-task-form-collaborator-${teamMemberDto.teamMemberId}`}
                                    boringAvatarKey={teamMemberDto.account.accountId || ""}
                                    url={teamMemberDto.account.profilePicture?.url}
                                    wrapperClassName={styles.profilePic}
                                />
                            ))}
                        </div>
                        {selectedCollaborators.length == 1
                            ? selectedCollaborators[0].account.username
                            : t("newTaskFormPickedCollaboratorsLabel")?.replace("${count}", `${selectedCollaborators.length}`)}
                    </div>
                }
                emptySelectionLabel={t("newTaskFormPickCollaboratorsButtonLabel")}
                onUnpickClick={deselect}
            />
        </div>
    );
};

export default forwardRef<ICollaboratorPickerButtonRef, CollaboratorPickerButtonProps>(CollaboratorPickerButton);
