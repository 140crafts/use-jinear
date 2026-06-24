import React from 'react';
import styles from './TeamPanel.module.css';
import TabbedPanel from "@/components/tabbedPanel/TabbedPanel.tsx";
import TabView from "@/components/tabbedPanel/tabView/TabView.tsx";
import useTranslation from "@/locals/useTranslation.ts";
import type {TeamDto, WorkspaceDto} from "@/be/jinear-core.ts";
import MultiViewTaskList, {
    getTeamDefaultDisplayFormat
} from "@/components/taskLists/multiViewTaskList/MultiViewTaskList.tsx";
import TaskBoardList from "@/components/taskLists/taskBoardList/TaskBoardList.tsx";
import TeamFileList from "@/components/teamFilesScreen/TeamFileList.tsx";
import {LuAlignStartHorizontal, LuFolder, LuSquareCheckBig} from "react-icons/lu";
import {hasWorkspaceFilePermissions} from "@/util/permissionHelper.ts";

interface TeamPanelProps {
    workspace: WorkspaceDto;
    team: TeamDto
}

const TeamPanel: React.FC<TeamPanelProps> = ({workspace, team}) => {
    const {t} = useTranslation();
    const displayFormat = getTeamDefaultDisplayFormat(team.username);

    return (
        <TabbedPanel>
            <TabView
                name="tasks"
                label={t("tasksScreenBreadcrumbLabel")}
                icon={<LuSquareCheckBig className={'icon'} size={16}/>}
                buttonClass={styles.tabButton}
            >
                <MultiViewTaskList
                    key={team.teamId}
                    workspace={workspace}
                    team={team}
                    activeDisplayFormat={displayFormat}
                    workflowStatusBoardClassName={styles.workflowStatusBoard}
                />
            </TabView>

            <TabView
                name="boards"
                label={t("taskBoardsListTitle")}
                icon={<LuAlignStartHorizontal className={'icon'} size={16}/>}
                buttonClass={styles.tabButton}
            >
                <TaskBoardList team={team} workspace={workspace}/>
            </TabView>

            {/*hasWorkspaceFilePermissions(workspace)*/}
            <TabView
                name="files"
                label={t("teamFilesListTitle")}
                icon={<LuFolder className={'icon'} size={16}/>}
                buttonClass={styles.tabButton}
            >
                <TeamFileList teamId={team.teamId}/>
            </TabView>

        </TabbedPanel>
    );
}

export default TeamPanel;