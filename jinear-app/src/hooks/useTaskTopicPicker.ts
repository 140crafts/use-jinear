import type {TaskDto, TopicDto} from "@/model/be/jinear-core";
import {useRemoveTaskTopicMutation, useUpdateTaskTopicMutation} from "@/store/api/taskTopicApi";
import {useRetrieveWorkspaceTeamsQuery} from "@/store/api/teamApi";
import {selectCurrentAccountsWorkspace} from "@/store/slice/accountSlice";
import {changeLoadingModalVisibility, popTopicPickerModal} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";

export const useTaskTopicPicker = (task?: TaskDto | null) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [updateTaskTopic] = useUpdateTaskTopicMutation();
    const [removeTaskTopic] = useRemoveTaskTopicMutation();

    const accountsWorkspace = useTypedSelector(selectCurrentAccountsWorkspace(task?.workspaceId));
    const {data: teamsResponse} = useRetrieveWorkspaceTeamsQuery(task?.workspaceId ?? "", {
        skip: task == null || task.team != null,
    });
    const workspace = task?.workspace ?? accountsWorkspace;
    const team = task?.team ?? teamsResponse?.data.find((teamDto) => teamDto.teamId == task?.teamId);

    const hideLoading = () => {
        dispatch(changeLoadingModalVisibility({visible: false}));
    };

    const popTopicPicker = () => {
        if (!task || !workspace || !team) {
            return;
        }
        const onPick = (pickedList: TopicDto[]) => {
            const nextTopicId = pickedList[0]?.topicId;
            if (!nextTopicId || nextTopicId == task.topicId) {
                return;
            }
            dispatch(changeLoadingModalVisibility({visible: true}));
            updateTaskTopic({taskId: task.taskId, topicId: nextTopicId}).finally(hideLoading);
        };
        const onRemove = () => {
            dispatch(changeLoadingModalVisibility({visible: true}));
            removeTaskTopic({taskId: task.taskId}).finally(hideLoading);
        };
        dispatch(
            popTopicPickerModal({
                visible: true,
                multiple: false,
                workspace,
                team,
                currentTopicId: task.topicId,
                title: t("topicPickerModalTaskTopicTitle"),
                onPick,
                onRemove
            })
        );
    };

    return {popTopicPicker, isReady: workspace != null && team != null};
};
