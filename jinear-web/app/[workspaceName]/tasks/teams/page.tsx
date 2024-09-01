"use client";
import React from "react";
import styles from "./index.module.css";
import { useRetrieveWorkspaceTeamsQuery } from "@/api/teamApi";
import { useParams } from "next/navigation";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";
import EndlessScrollList from "@/components/endlessScrollList/EndlessScrollList";
import { TeamDto, WorkspaceDto } from "@/be/jinear-core";
import WorkspaceTeamsListItem from "@/components/workspaceTeamsScreen/workspaceTeamsListItem/WorkspaceTeamsListItem";
import useTranslation from "@/locals/useTranslation";
import WorkspaceTeamsListTitle from "@/components/workspaceTeamsScreen/workspaceTeamsListTitle/WorkspaceTeamsListTitle";

interface ProjectsPageProps {

}

const TeamsPage: React.FC<ProjectsPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName)) as WorkspaceDto;
  const {
    data: teamsResponse,
    isFetching: isTeamsFetching
  } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null
  });

  const renderItem = (item: TeamDto) => <WorkspaceTeamsListItem key={`ws-team-${item.teamId}`} team={item}
                                                                workspace={workspace} />;

  return (
    <div className={styles.container}>
      <EndlessScrollList
        id={"teams-page-list"}
        data={teamsResponse?.data || []}
        isFetching={isTeamsFetching}
        renderItem={renderItem}
        emptyLabel={t("workspaceTeamListEmpty")}
        hasMore={false}
        listTitleComponent={<WorkspaceTeamsListTitle workspace={workspace} teamCount={teamsResponse?.data?.length} />}
      />
    </div>
  );
};

export default TeamsPage;