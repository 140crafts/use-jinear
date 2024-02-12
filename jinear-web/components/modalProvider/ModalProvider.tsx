"use client";
import ErrorBoundary from "@/components/errorBoundary/ErrorBoundary";
import React from "react";

import AccountProfileModal from "@/components/modal/accountProfileModal/AccountProfileModal";
import AddMemberToTeamModal from "@/components/modal/addMemberToTeamModal/AddMemberToTeamModal";
import BasicTextInputModal from "@/components/modal/basicTextInputModal/BasicTextInputModal";
import BoardPickerModal from "@/components/modal/boardPickerModal/BoardPickerModal";
import DatePickerModal from "@/components/modal/datePicker/DatePickerModal";
import DialogModal from "@/components/modal/dialogModal/DialogModal";
import LoadingModal from "@/components/modal/loadingModal/LoadingModal";
import LoginWith2FaMailModal from "@/components/modal/loginWith2FaMailModal/LoginWith2FaMailModal";
import MenuMoreActionModal from "@/components/modal/menuMoreActionModal/MenuMoreActionModal";
import NewTaskBoardModal from "@/components/modal/newTaskBoardModal/NewTaskBoardModal";
import NewTaskModal from "@/components/modal/newTaskModal/NewTaskModal";
import NewTeamModal from "@/components/modal/newTeamModal/NewTeamModal";
import NewTopicModal from "@/components/modal/newTopicModal/NewTopicModal";
import NewWorkspaceModal from "@/components/modal/newWorkspaceModal/NewWorkspaceModal";
import NotFoundModal from "@/components/modal/notFoundModal/NotFoundModal";
import NotificationPermissionModal from "@/components/modal/notificationPermissionModal/NotificationPermissionModal";
import NewReminderModal from "@/components/modal/reminder/NewReminderModal/NewReminderModal";
import ReminderListModal from "@/components/modal/reminder/reminderListModal/ReminderListModal";
import SearchTaskModal from "@/components/modal/searchTaskModal/SearchTaskModal";
import TaskAssigneeChangeModal from "@/components/modal/taskDetailModals/taskAssigneeChangeModal/TaskAssigneeChangeModal";
import TaskDateChangeModal from "@/components/modal/taskDetailModals/taskDateChangeModal/TaskDateChangeModal";
import TaskTopicChangeModal from "@/components/modal/taskDetailModals/taskTopicChangeModal/TaskTopicChangeModal";
import WorkflowChangeStatusModal from "@/components/modal/taskDetailModals/workflowChangeStatus/WorkflowChangeStatusModal";
import TaskOverviewModal from "@/components/modal/taskOverviewModal/TaskOverviewModal";
import TaskTaskBoardModal from "@/components/modal/taskTaskBoardModal/TaskTaskBoardModal";
import TeamMemberPickerModal from "@/components/modal/teamMemberPickerModal/TeamMemberPickerModal";
import TeamPickerModal from "@/components/modal/teamPickerModal/TeamPickerModal";
import TeamWorkflowStatusModal from "@/components/modal/teamWorkflowStatusModal/TeamWorkflowStatusModal";
import TopicPickerModal from "@/components/modal/topicPickerModal/TopicPickerModal";
import UpgradeWorkspaceModal from "@/components/modal/upgradeWorkspaceModal/UpgradeWorkspaceModal";
import WorkspaceInviteMemberModal from "@/components/modal/workspaceInviteMemberModal/WorkspaceInviteMemberModal";
import WorkspacePickerModal from "@/components/modal/workspacePickerModal/WorkspacePickerModal";
import WorkspaceSwitchModal from "@/components/modal/workspaceSwitchModal/WorkspaceSwitchModal";
import CalendarExternalEventViewModal from "../modal/calendarExternalEventViewModal/CalendarExternalEventViewModal";
import DeviceOfflineModal from "../modal/deviceOfflineModal/DeviceOfflineModal";
import IntegrationFeedItemDetailModal from "../modal/integrationFeedItemDetailModal/IntegrationFeedItemDetailModal";
import NewCalendarIntegrationModal from "../modal/newCalendarIntegrationModal/NewCalendarIntegrationModal";
import NewMailIntegrationModal from "../modal/newMailIntegrationModal/NewMailIntegrationModal";
import WorkspaceMemberPickerModal from "../modal/workspaceMemberPickerModal/WorkspaceMemberPickerModal";

interface ModalProviderProps {}

const globalModals: any = (
  <>
    <CalendarExternalEventViewModal />
    <WorkspaceMemberPickerModal />
    <NewCalendarIntegrationModal />
    <NewMailIntegrationModal />
    <MenuMoreActionModal />
    <WorkspaceSwitchModal />
    <AccountProfileModal />
    <TaskOverviewModal />
    <NotificationPermissionModal />
    <AddMemberToTeamModal />
    <WorkspaceInviteMemberModal />
    <ReminderListModal />
    <NewReminderModal />
    <SearchTaskModal />
    <NewTeamModal />
    <NewWorkspaceModal />
    <TaskAssigneeChangeModal />
    <TaskDateChangeModal />
    <TaskTopicChangeModal />
    <TaskTaskBoardModal />
    <WorkflowChangeStatusModal />
    <IntegrationFeedItemDetailModal />
    <NewTaskModal />
    <NewTaskBoardModal />
    <TeamPickerModal />
    <WorkspacePickerModal />
    <TeamMemberPickerModal />
    <TopicPickerModal />
    <TeamWorkflowStatusModal />
    <BoardPickerModal />
    <NewTopicModal />
    <UpgradeWorkspaceModal />
    <LoginWith2FaMailModal />
    <DatePickerModal />
    <BasicTextInputModal />
    <DialogModal />
    <NotFoundModal />
    <DeviceOfflineModal />
    <LoadingModal />
  </>
);

const ModalProvider: React.FC<ModalProviderProps> = ({}) => {
  return <ErrorBoundary message={"Global Modals"}>{globalModals}</ErrorBoundary>;
};

export default ModalProvider;
