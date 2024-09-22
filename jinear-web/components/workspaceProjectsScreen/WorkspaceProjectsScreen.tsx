import React, { useState } from "react";
import styles from "./WorkspaceProjectsScreen.module.css";
import { useAllProjectsQuery, useArchivedProjectsQuery, useRetrieveAssignedProjectsQuery } from "@/api/projectQueryApi";
import { ProjectDto, WorkspaceDto } from "@/be/jinear-core";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import ProjectsTitle from "@/components/workspaceProjectsScreen/projectsTitle/ProjectsTitle";
import useTranslation from "@/locals/useTranslation";
import ProjectRow from "@/components/workspaceProjectsScreen/projectRow/ProjectRow";
import { useWorkspaceRole } from "@/hooks/useWorkspaceRole";

interface WorkspaceProjectsScreenProps {
  workspace: WorkspaceDto;
}

const WorkspaceProjectsScreen: React.FC<WorkspaceProjectsScreenProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const [allProjectsPage, setAllProjectsPage] = useState<number>(0);
  const [archivedProjectsPage, setArchivedProjectsPage] = useState<number>(0);
  const workspaceRole = useWorkspaceRole({ workspaceId: workspace.workspaceId });
  const shouldListArchived = ["ADMIN", "OWNER"].includes(`${workspaceRole}`);

  const {
    data: allProjectsResponse,
    isFetching: isAllProjectsFetching,
    isLoading: isAllProjectsLoading
  } = useAllProjectsQuery({ workspaceId: workspace.workspaceId, page: allProjectsPage });

  const {
    data: archivedProjectsResponse,
    isFetching: isArchivedProjectsFetching,
    isLoading: isArchivedProjectsLoading
  } = useArchivedProjectsQuery({
    workspaceId: workspace.workspaceId,
    page: archivedProjectsPage
  }, { skip: !shouldListArchived });

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

      {shouldListArchived && archivedProjectsResponse?.data?.hasContent &&
        <div className={styles.archivedProjectsContainer}>
          <PaginatedList
            id={"archived-projects-paginated"}
            data={archivedProjectsResponse?.data}
            isFetching={isArchivedProjectsFetching}
            isLoading={isArchivedProjectsLoading}
            page={archivedProjectsPage}
            setPage={setArchivedProjectsPage}
            renderItem={renderAllItem}
            emptyLabel={t("projectsAllListEmptyLabel")}
            listTitle={t("projectsArchivedTitle")?.replace("${num}", `${archivedProjectsResponse?.data?.totalElements}`)}
            hidePaginationOnSinglePages={true}
          />
        </div>
      }

    </div>
  );
};

export default WorkspaceProjectsScreen;