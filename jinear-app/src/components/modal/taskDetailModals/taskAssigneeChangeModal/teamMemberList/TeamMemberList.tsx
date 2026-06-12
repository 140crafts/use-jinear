import Button, {ButtonVariants} from "@/components/button";
import {useUpdateTaskAssigneeMutation} from "@/store/api/taskUpdateApi";
import {useRetrieveTeamMembersQuery} from "@/store/api/teamMemberApi";
import {
    changeLoadingModalVisibility,
    selectChangeTaskAssigneeModalTaskCurrentAssigneeId,
    selectChangeTaskAssigneeModalTaskId,
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./TeamMemberList.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TeamMemberListProps {
    teamId: string;
    filter: string;
    close: () => void;
}

const TeamMemberList: React.FC<TeamMemberListProps> = ({teamId, filter, close}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {currentData: teamMemberListResponse, isFetching} = useRetrieveTeamMembersQuery(
        {teamId},
        {
            skip: teamId == null,
        }
    );
    const taskId = useTypedSelector(selectChangeTaskAssigneeModalTaskId);
    const taskCurrentAssignee = useTypedSelector(selectChangeTaskAssigneeModalTaskCurrentAssigneeId);

    const [updateTaskAssignee, {isSuccess, isError}] = useUpdateTaskAssigneeMutation();

    useEffect(() => {
        if (isSuccess || isError) {
            dispatch(changeLoadingModalVisibility({visible: false}));
        }
        if (isSuccess) {
            close();
        }
    }, [isSuccess, isError]);

    const filteredList =
        teamMemberListResponse?.data.content.filter(
            (member) => filter == "" || member.account.username?.toLowerCase().indexOf(filter?.toLowerCase()) != -1
        ) || [];

    const changeTaskAssignee = (accountId: string) => {
        if (accountId == taskCurrentAssignee) {
            close();
            return;
        }
        if (taskId) {
            dispatch(changeLoadingModalVisibility({visible: true}));
            updateTaskAssignee({taskId, body: {assigneeId: accountId}});
        }
    };

    return (
        <div className={styles.container}>
            {isFetching && teamMemberListResponse == null && (
                <div className={styles.centeredInfo}>
                    <CircularLoading size={17}/>
                </div>
            )}
            {filteredList.map((member) => (
                <Button
                    key={`task-assignee-modal-${member.accountId}`}
                    className={styles.button}
                    variant={taskCurrentAssignee == member.accountId ? ButtonVariants.filled2 : ButtonVariants.filled}
                    onClick={() => changeTaskAssignee(member.accountId)}
                >
                    {member.account.username}
                </Button>
            ))}

            {filteredList.length == 0 && teamMemberListResponse && (
                <div className={styles.centeredInfo}>
                    {t("changeTaskAssigneeModalFilteredListEmpty")}
                </div>
            )}
        </div>
    );
};

export default TeamMemberList;
