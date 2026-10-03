import type {TaskDto, TeamMemberDto} from "@/model/be/jinear-core";
import {useRetrieveTeamMembersQuery} from "@/store/api/teamMemberApi";
import {useUpdateTaskCollaboratorsMutation} from "@/store/api/taskUpdateApi";
import {popTeamMemberPickerModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";

export const useTaskCollaboratorPicker = (task?: TaskDto | null) => {
    const dispatch = useAppDispatch();
    const [updateTaskCollaborators, {isLoading}] = useUpdateTaskCollaboratorsMutation();

    const collaborators = task?.taskCollaborators ?? [];
    const {data: teamMemberListResponse} = useRetrieveTeamMembersQuery(
        {teamId: task?.teamId ?? ""},
        {skip: task?.teamId == null}
    );
    const selectedTeamMembers = teamMemberListResponse?.data.content.filter(
        (teamMemberDto) => collaborators.findIndex((collaborator) => collaborator.accountId == teamMemberDto.accountId) != -1
    );

    const popCollaboratorPicker = () => {
        if (!task) {
            return;
        }
        const onPick = (pickedList: TeamMemberDto[]) => {
            updateTaskCollaborators({
                taskId: task.taskId,
                body: {collaboratorIds: pickedList.map((teamMemberDto) => teamMemberDto.accountId)}
            });
        };
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

    return {popCollaboratorPicker, isLoading, isReady: teamMemberListResponse != null};
};
