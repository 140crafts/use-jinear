import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'
import LoginPage from '@/pages/login/LoginPage.tsx'
import AppPage from '@/pages/app/AppPage.tsx'
import ModalProvider from "@/components/modal-provider/ModalProvider.tsx";
import FirebaseProvider from "@/components/firebaseProvider/FirebaseProvider.tsx";
import DateFnsConfigration from "@/components/dateFnsConfigration/DateFnsConfigration.tsx";
import AuthCheck from "@/components/authCheck/AuthCheck.tsx";
import ForcePasswordChangeChecker from "@/components/forcePasswordChangeChecker/ForcePasswordChangeChecker.tsx";
import WorkspaceAndTeamChangeListener
    from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener.tsx";
import OnboardListener from "@/components/onboardListener/OnboardListener.tsx";
import WebViewEventListener from "@/components/webViewEventListener/WebViewEventListener.tsx";
import PostHogPageView from "@/components/postHogPageView/PostHogPageView.tsx";
import ToasterProvider from "@/components/toasterProvider/ToasterProvider.tsx";
import BodyFixer from "@/components/bodyFixer/BodyFixer.tsx";
import RegisterPage from "@/pages/register/page.tsx";
import ProfileScreen from "@/pages/profile/page.tsx";
import ForgotPasswordPage from "@/pages/forgot-password/page.tsx";
import NewWorkspaceScreen from "@/pages/new-workspace/page.tsx";
import ConfirmEmailPage from "@/pages/engage/confirm-email/page.tsx";
import DeleteAccountCompletePage from "@/pages/engage/delete-account-complete/page.tsx";
import ResetPasswordCompletePage from "@/pages/engage/reset-password-complete/page.tsx";
import WorkspaceInvitationResponseScreen from "@/pages/engage/workspace-invitation/page.tsx";
import WorkspaceLayout from "@/pages/workspace/workspace-layout/layout.tsx";
import WorkspacePage from "@/pages/workspace/workspace-home/page.tsx";
import CalendarLayout from "@/pages/workspace/calendar/calendar-layout/layout.tsx";
import CalendarPage from "@/pages/workspace/calendar/calendar-page/page.tsx";
import CalendarMembersPage from "@/pages/workspace/calendar/calendar-id/members/page.tsx";
import CalendarSettingsPage from "@/pages/workspace/calendar/calendar-id/settings/page.tsx";
import FilesLayout from "@/pages/workspace/files/files-layout/layout.tsx";
import FilesPage from "@/pages/workspace/files/files-page/page.tsx";
import InboxScreen from "@/pages/workspace/inbox/page.tsx";
import WorkspaceMembersScreen from "@/pages/workspace/members/page.tsx";
import WorkspaceSettingsScreen from "@/pages/workspace/settings/page.tsx";
import TaskDetailPage from "@/pages/workspace/task/task-detail/page.tsx";
import TasksLayout from "@/pages/workspace/tasks/tasks-layout/layout.tsx";
import WorkspaceTasksPage from "@/pages/workspace/tasks/tasks-page/page.tsx";
import TeamPage from "@/pages/workspace/tasks/team-username/team-directory-page/page.tsx";
import FilesScreen from "@/pages/workspace/tasks/team-username/files/page.tsx";
import TeamMembersScreen from "@/pages/workspace/tasks/team-username/members/page.tsx";
import SettingsScreen from "@/pages/workspace/tasks/team-username/settings/page.tsx";
import TaskBoardListScreen from "@/pages/workspace/tasks/team-username/task-boards/page.tsx";
import TaskBoardDetailScreen from "@/pages/workspace/tasks/team-username/task-boards/taskBoard-id/page.tsx";
import TasksScreen from "@/pages/workspace/tasks/team-username/tasks/page.tsx";
import EditTopicScreen from "@/pages/workspace/tasks/team-username/topic/edit/topic-id/page.tsx";
import TeamTopicListScreen from "@/pages/workspace/tasks/team-username/topic/list/page.tsx";
import NewTopicPage from "@/pages/workspace/tasks/team-username/topic/new/page.tsx";
import AssignedToMePage from "@/pages/workspace/tasks/assigned-to-me/page.tsx";
import LastActivitiesScreen from "@/pages/workspace/tasks/last-activities/page.tsx";
import TeamsPage from "@/pages/workspace/tasks/teams/page.tsx";
import ReloadQueryRefetchHandler from "@/components/reloadQueryRefetchHandler/ReloadQueryRefetchHandler.tsx";

export default function App() {
    return (
        <BrowserRouter>
            {/*<OfflineBanner/>*/}
            <DateFnsConfigration/>
            <FirebaseProvider/>
            <AuthCheck/>
            <ForcePasswordChangeChecker/>
            <WorkspaceAndTeamChangeListener/>
            <OnboardListener/>
            <WebViewEventListener/>
            <PostHogPageView/>
            <ToasterProvider/>
            <BodyFixer/>
            <ModalProvider/>
            {/*<ReloadQueryRefetchHandler/>*/}
            <Routes>

                <Route path="*" element={<Navigate to="/" replace/>}/>
                <Route path="/" element={<AppPage/>}/>

                <Route path="/forgot-password" element={<ForgotPasswordPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/profile" element={<ProfileScreen/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/new-workspace" element={<NewWorkspaceScreen/>}/>

                <Route path={'/engage'}>
                    <Route path={'confirm-email'} element={<ConfirmEmailPage/>}/>
                    <Route path={'delete-account-complete'} element={<DeleteAccountCompletePage/>}/>
                    <Route path={'reset-password-complete'} element={<ResetPasswordCompletePage/>}/>
                    <Route path={'workspace-invitation'} element={<WorkspaceInvitationResponseScreen/>}/>
                </Route>

                <Route path="/:workspaceName" element={<WorkspaceLayout/>}>
                    <Route index element={<WorkspacePage/>}/>

                    <Route path="inbox" element={<InboxScreen/>}/>
                    <Route path="members" element={<WorkspaceMembersScreen/>}/>
                    <Route path="settings" element={<WorkspaceSettingsScreen/>}/>
                    <Route path="task/:taskTag" element={<TaskDetailPage/>}/>


                    <Route path="tasks" element={<TasksLayout/>}>
                        <Route index element={<WorkspaceTasksPage/>}/>
                        <Route path="assigned-to-me" element={<AssignedToMePage/>}/>
                        <Route path="last-activities" element={<LastActivitiesScreen/>}/>
                        <Route path="teams" element={<TeamsPage/>}/>
                        <Route path=":teamUsername">
                            <Route index element={<TeamPage/>}/>
                            <Route path="files" element={<FilesScreen/>}/>
                            <Route path="members" element={<TeamMembersScreen/>}/>
                            <Route path="settings" element={<SettingsScreen/>}/>
                            <Route path="task-boards">
                                <Route index element={<TaskBoardListScreen/>}/>
                                <Route path=":taskBoardId" element={<TaskBoardDetailScreen/>}/>
                            </Route>
                            <Route path="tasks" element={<TasksScreen/>}/>
                            <Route path="topic">
                                <Route index element={<Navigate to="list" replace/>}/>
                                <Route path="list" element={<TeamTopicListScreen/>}/>
                                <Route index path="new" element={<NewTopicPage/>}/>
                                <Route path="edit/:topicId" element={<EditTopicScreen/>}/>
                            </Route>

                        </Route>
                    </Route>

                    <Route path="calendar" element={<CalendarLayout/>}>
                        <Route index element={<CalendarPage/>}/>
                        <Route path=":calendarId">
                            <Route index path="members" element={<CalendarMembersPage/>}/>
                            <Route path="settings" element={<CalendarSettingsPage/>}/>
                        </Route>
                    </Route>

                    <Route path="files" element={<FilesLayout/>}>
                        <Route index element={<FilesPage/>}/>
                    </Route>

                </Route>

            </Routes>
        </BrowserRouter>
    )
}
