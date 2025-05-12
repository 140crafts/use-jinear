import { TaskMediaDto } from "@/model/be/jinear-core";
import { useRetrieveTaskMediaListFromTeamQuery } from "@/store/api/taskMediaApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import PaginatedList from "../paginatedList/PaginatedList";
import styles from "./TeamFileList.module.css";
import FileRow from "./fileRow/FileRow";

interface TeamFileListProps {
  teamId: string;
}

const TeamFileList: React.FC<TeamFileListProps> = ({ teamId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: teamFilesResponse, isFetching, isLoading } = useRetrieveTaskMediaListFromTeamQuery({ teamId, page });

  const renderItem = (data: TaskMediaDto, index: number) => {
    const relatedTaskDifferentFromOneBefore =
      index == 0 || teamFilesResponse?.data?.content?.[index - 1].task?.taskId != data.task.taskId;

    return (
      <FileRow
        key={`task-file-list-row-${index}`}
        index={index}
        relatedTaskDifferentFromOneBefore={relatedTaskDifferentFromOneBefore}
        data={data}
      />
    );
  };

  return (
    <div className={styles.container}>
      <PaginatedList
        id={"team-file-list-paginated"}
        data={teamFilesResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("teamFilesListEmptyLabel")}
        listTitle={t("teamFilesListTitle")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
      />
    </div>
  );
};

export default TeamFileList;
