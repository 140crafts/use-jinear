import { AccountDto } from "@/model/be/jinear-core";
import { useRetrieveWithAssigneeQuery } from "@/store/api/taskListingApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PaginatedAssigneeTaskListProps {
  teamId: string;
  workspaceId: string;
}

const PaginatedAssigneeTaskList: React.FC<PaginatedAssigneeTaskListProps> = ({ teamId, workspaceId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const [assignee, setAssignee] = useState<AccountDto | null>(currentAccount);

  useEffect(() => {
    if (currentAccount) {
      setAssignee(currentAccount);
    }
  }, [currentAccount]);

  const {
    data: response,
    isSuccess,
    isError,
    isFetching,
    isLoading,
  } = useRetrieveWithAssigneeQuery(
    {
      teamId,
      workspaceId,
      assigneeId: currentAccount?.accountId || "",
      page,
    },
    { skip: currentAccount == null }
  );

  return (
    <BaseTaskList
      id={`assignee-tasks-${workspaceId}-${teamId}-${currentAccount?.accountId}`}
      name={t("assigneeTaskListName").replace("${assigneeName}", currentAccount?.username || "")}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default PaginatedAssigneeTaskList;
