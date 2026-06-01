import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'
import LoginPage from '@/pages/login/LoginPage.tsx'
import AppPage from '@/pages/app/AppPage.tsx'
import OfflineBanner from '@/components/offline-banner/OfflineBanner.tsx'
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

                    <Route path="calendar" element={<CalendarLayout/>}>
                        <Route index element={<CalendarPage/>}/>
                        <Route path=":calendarId">
                            <Route index path="members" element={<CalendarMembersPage/>}/>
                            <Route path="settings" element={<CalendarSettingsPage/>}/>
                        </Route>
                    </Route>

                </Route>

            </Routes>
        </BrowserRouter>
    )
}
