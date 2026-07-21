import ErrorBoundary from "@/components/error-boundary/ErrorBoundary";
import MenuMoreActionModal from "@/components/modal/menuMoreActionModal/MenuMoreActionModal";
import React from "react";
import MaterialAccessModal from "@/components/modal/materialAccessModal/MaterialAccessModal.tsx";
import MaterialFolderPickerModal from "@/components/modal/materialFolderPickerModal/MaterialFolderPickerModal.tsx";
import ShareWorkspaceEventsModal from "@/components/modal/shareWorkspaceEventsModal/ShareWorkspaceEventsModal.tsx";
import CalendarExternalEventViewModal
    from "@/components/modal/calendarExternalEventViewModal/CalendarExternalEventViewModal.tsx";
import NewMailIntegrationModal from "@/components/modal/newMailIntegrationModal/NewMailIntegrationModal.tsx";
import WorkspaceSwitchModal from "@/components/modal/workspaceSwitchModal/WorkspaceSwitchModal.tsx";
import AccountProfileModal from "@/components/modal/accountProfileModal/AccountProfileModal.tsx";
import TaskOverviewModal from "@/components/modal/taskOverviewModal/TaskOverviewModal.tsx";
import NotificationPermissionModal
    from "@/components/modal/notificationPermissionModal/NotificationPermissionModal.tsx";
import AddMemberToTeamModal from "@/components/modal/addMemberToTeamModal/AddMemberToTeamModal.tsx";
import WorkspaceInviteMemberModal from "@/components/modal/workspaceInviteMemberModal/WorkspaceInviteMemberModal.tsx";
import ReminderListModal from "@/components/modal/reminder/reminderListModal/ReminderListModal.tsx";
import NewReminderModal from "@/components/modal/reminder/NewReminderModal/NewReminderModal.tsx";
import SearchTaskModal from "@/components/modal/searchTaskModal/SearchTaskModal.tsx";
import NewTeamModal from "@/components/modal/newTeamModal/NewTeamModal.tsx";
import NewWorkspaceModal from "@/components/modal/newWorkspaceModal/NewWorkspaceModal.tsx";
import TaskAssigneeChangeModal
    from "@/components/modal/taskDetailModals/taskAssigneeChangeModal/TaskAssigneeChangeModal.tsx";
import TaskDateChangeModal from "@/components/modal/taskDetailModals/taskDateChangeModal/TaskDateChangeModal.tsx";
import TaskTopicChangeModal from "@/components/modal/taskDetailModals/taskTopicChangeModal/TaskTopicChangeModal.tsx";
import TaskTaskBoardModal from "@/components/modal/taskTaskBoardModal/TaskTaskBoardModal.tsx";
import WorkflowChangeStatusModal
    from "@/components/modal/taskDetailModals/workflowChangeStatus/WorkflowChangeStatusModal.tsx";
import NewTaskModal from "@/components/modal/newTaskModal/NewTaskModal.tsx";
import WorkspaceMemberPickerModal from "@/components/modal/workspaceMemberPickerModal/WorkspaceMemberPickerModal.tsx";
import NewCalendarIntegrationModal
    from "@/components/modal/newCalendarIntegrationModal/NewCalendarIntegrationModal.tsx";
import NewTaskBoardModal from "@/components/modal/newTaskBoardModal/NewTaskBoardModal.tsx";
import TeamPickerModal from "@/components/modal/teamPickerModal/TeamPickerModal.tsx";
import TeamPickerModalV2 from "@/components/modal/teamPickerModalV2/TeamPickerModalV2.tsx";
import WorkspacePickerModal from "@/components/modal/workspacePickerModal/WorkspacePickerModal.tsx";
import TeamMemberPickerModal from "@/components/modal/teamMemberPickerModal/TeamMemberPickerModal.tsx";
import TopicPickerModal from "@/components/modal/topicPickerModal/TopicPickerModal.tsx";
import TeamWorkflowStatusModal from "@/components/modal/teamWorkflowStatusModal/TeamWorkflowStatusModal.tsx";
import BoardPickerModal from "@/components/modal/boardPickerModal/BoardPickerModal.tsx";
import NewTopicModal from "@/components/modal/newTopicModal/NewTopicModal.tsx";
import UpgradeWorkspaceModal from "@/components/modal/upgradeWorkspaceModal/UpgradeWorkspaceModal.tsx";
import DatePickerModal from "@/components/modal/datePicker/DatePickerModal.tsx";
import BasicTextInputModal from "@/components/modal/basicTextInputModal/BasicTextInputModal.tsx";
import UploadStatusModal from "@/components/modal/uploadStatusModal/UploadStatusModal.tsx";
import PasswordChangeModal from "@/components/modal/passwordChangeModal/PasswordChangeModal.tsx";
import NotebookInitializeModal from "@/components/modal/notebookInitializeModal/NotebookInitializeModal.tsx";
import NotebookPickerModal from "@/components/modal/notebookPickerModal/NotebookPickerModal.tsx";

interface WorkspaceModalProviderProps {
}

const workspaceModals: any = (
    <>
        <MenuMoreActionModal/>
        <MaterialAccessModal/>
        <MaterialFolderPickerModal/>

        <ShareWorkspaceEventsModal/>
        <CalendarExternalEventViewModal/>
        <NewMailIntegrationModal/>
        <WorkspaceSwitchModal/>
        <AccountProfileModal/>
        <TaskOverviewModal/>
        <NotificationPermissionModal/>
        <AddMemberToTeamModal/>
        <WorkspaceInviteMemberModal/>
        <ReminderListModal/>
        <NewReminderModal/>
        <SearchTaskModal/>
        <NewTeamModal/>
        <NewWorkspaceModal/>
        <TaskAssigneeChangeModal/>
        <TaskDateChangeModal/>
        <TaskTopicChangeModal/>
        <TaskTaskBoardModal/>
        <WorkflowChangeStatusModal/>
        <NewTaskModal/>

        <WorkspaceMemberPickerModal/>
        <NewCalendarIntegrationModal/>
        <NewTaskBoardModal/>

        <TeamPickerModal/>
        <TeamPickerModalV2/>
        <WorkspacePickerModal/>
        <TeamMemberPickerModal/>
        <TopicPickerModal/>
        <TeamWorkflowStatusModal/>
        <BoardPickerModal/>
        <NewTopicModal/>
        <UpgradeWorkspaceModal/>
        <NotebookInitializeModal/>
        <NotebookPickerModal/>

        <PasswordChangeModal/>
        <UploadStatusModal/>

    </>
);

const WorkspaceModalProvider: React.FC<WorkspaceModalProviderProps> = ({}) => {
    return (
        <ErrorBoundary message={"An unexpected error occurred while rendering workspace modals."}>
            {workspaceModals}
        </ErrorBoundary>);
};

export default WorkspaceModalProvider;
