import Button, { ButtonVariants } from "@/components/button";
import Transition from "@/components/transition/Transition";
import { useUpdateTaskAssigneeMutation } from "@/store/api/taskUpdateApi";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import {
  changeLoadingModalVisibility,
  selectChangeTaskAssigneeModalTaskCurrentAssigneeId,
  selectChangeTaskAssigneeModalTaskId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./TeamMemberList.module.css";

interface TeamMemberListProps {
  teamId: string;
  filter: string;
  close: () => void;
}

const TeamMemberList: React.FC<TeamMemberListProps> = ({ teamId, filter, close }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { data: teamMemberListResponse, isFetching } = useRetrieveTeamMembersQuery(
    { teamId },
    {
      skip: teamId == null,
    }
  );
  const taskId = useTypedSelector(selectChangeTaskAssigneeModalTaskId);
  const taskCurrentAssignee = useTypedSelector(selectChangeTaskAssigneeModalTaskCurrentAssigneeId);

  const [updateTaskAssignee, { isSuccess, isError }] = useUpdateTaskAssigneeMutation();

  useEffect(() => {
    if (isSuccess || isError) {
      dispatch(changeLoadingModalVisibility({ visible: false }));
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
      dispatch(changeLoadingModalVisibility({ visible: true }));
      updateTaskAssignee({ taskId, body: { assigneeId: accountId } });
    }
  };

  return (
    <div className={styles.container}>
      {isFetching && (
        <div className={styles.centeredInfo}>
          <CircularProgress size={17} />
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

      {filteredList.length == 0 && !isFetching && (
        <Transition initial={true} className={styles.centeredInfo}>
          {t("changeTaskAssigneeModalFilteredListEmpty")}
        </Transition>
      )}
    </div>
  );
};

export default TeamMemberList;
