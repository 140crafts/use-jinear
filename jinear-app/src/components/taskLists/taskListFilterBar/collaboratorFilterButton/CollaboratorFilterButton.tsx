import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";
import type {TeamMemberDto} from "@/model/be/jinear-core";
import {useRetrieveTeamMembersQuery} from "@/store/api/teamMemberApi";
import {popTeamMemberPickerModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {useTeam} from "../context/TaskListFilterBarContext";
import styles from "./CollaboratorFilterButton.module.css";
import {LuUsers} from "react-icons/lu";

interface CollaboratorFilterButtonProps {
}

const CollaboratorFilterButton: React.FC<CollaboratorFilterButtonProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const team = useTeam();

    const setQueryState = useSetQueryState();
    const selectedCollaboratorIds = useQueryState<string[]>("collaboratorIds", queryStateArrayParser);
    const isEmpty = selectedCollaboratorIds == null || selectedCollaboratorIds.length == 0;

    const {data: teamMemberListResponse} = useRetrieveTeamMembersQuery(
        {teamId: team?.teamId || ""},
        {
            skip: team == null
        }
    );
    const selectedCollaborators = teamMemberListResponse?.data.content.filter(
        (member) => selectedCollaboratorIds && selectedCollaboratorIds.indexOf(member.accountId) != -1
    );

    const onPick = (pickedList: TeamMemberDto[]) => {
        const pickedIds = pickedList.map((teamMemberDto) => teamMemberDto.accountId);
        setQueryState("collaboratorIds", queryStateAnyToStringConverter(pickedIds));
    };

    const popPicker = () => {
        dispatch(
            popTeamMemberPickerModal({
                visible: true,
                teamId: team?.teamId,
                multiple: true,
                initialSelectionOnMultiple: selectedCollaborators,
                onPick
            })
        );
    };

    return (
        <Button
            heightVariant={ButtonHeight.short}
            variant={isEmpty ? ButtonVariants.default : ButtonVariants.filled2}
            onClick={popPicker}
        >
            {isEmpty ? (
                <div className={styles.singleItem}>
                    <LuUsers/>
                    {t("taskFilterCollaboratorFilterButtonEmpty")}
                </div>
            ) : (
                <b>
                    {selectedCollaborators?.length == 1 ? (
                        <div className={styles.singleItem}>
                            {t("taskFilterCollaboratorFilterButtonSingleSelection")}
                            <ProfilePhoto
                                boringAvatarKey={selectedCollaborators[0]?.account.accountId || ""}
                                url={selectedCollaborators[0]?.account.profilePicture?.url}
                                wrapperClassName={styles.profilePic}
                            />
                            {selectedCollaborators[0]?.account.username}
                        </div>
                    ) : (
                        t("taskFilterCollaboratorFilterButtonSelected")?.replace("${count}", `${selectedCollaborators?.length}`)
                    )}
                </b>
            )}
        </Button>
    );
};

export default CollaboratorFilterButton;
