import React, { useState } from "react";
import styles from "./WorkspaceProjectsScreen.module.css";
import { useAllProjectsQuery, useRetrieveAssignedProjectsQuery } from "@/api/projectQueryApi";
import { ProjectDto, WorkspaceDto } from "@/be/jinear-core";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import ProjectsTitle from "@/components/workspaceProjectsScreen/projectsTitle/ProjectsTitle";
import useTranslation from "@/locals/useTranslation";
import ProjectRow from "@/components/workspaceProjectsScreen/projectRow/ProjectRow";

interface WorkspaceProjectsScreenProps {
  workspace: WorkspaceDto;
}

const WorkspaceProjectsScreen: React.FC<WorkspaceProjectsScreenProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const [allProjectsPage, setAllProjectsPage] = useState<number>(0);

  const {
    data: allProjectsResponse,
    isFetching: isAllProjectsFetching,
    isLoading: isAllProjectsLoading
  } = useAllProjectsQuery({ workspaceId: workspace.workspaceId });

  const renderAllItem = (item: ProjectDto) => {
    return (<ProjectRow key={`project-list-item-${item.projectId}`} project={item} />);
  };

  return (
    <div className={styles.container}>
      <PaginatedList
        id={"all-projects-paginated"}
        data={allProjectsResponse?.data}
        isFetching={isAllProjectsFetching}
        isLoading={isAllProjectsLoading}
        page={allProjectsPage}
        setPage={setAllProjectsPage}
        renderItem={renderAllItem}
        emptyLabel={t("projectsAllListEmptyLabel")}
        listTitle={t("projectsAllTitle")}
        hidePaginationOnSinglePages={true}
        listTitleComponent={workspace && <ProjectsTitle workspace={workspace} />}
      />
    </div>
  );
};

export default WorkspaceProjectsScreen;