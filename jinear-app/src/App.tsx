import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom'
import LoginPage from '@/pages/auth/LoginPage.tsx'
import RegisterPage from '@/pages/auth/RegisterPage.tsx'
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

export default function App() {
    return (
        <BrowserRouter>
            <OfflineBanner/>
            <DateFnsConfigration/>
            <FirebaseProvider/>
            <AuthCheck/>
            <ForcePasswordChangeChecker/>
            <WorkspaceAndTeamChangeListener/>
            <OnboardListener/>
            <WebViewEventListener/>
            <PostHogPageView/>
            <ToasterProvider/>
            <BodyFixer />
            <ModalProvider/>
            <Routes>
                <Route path="/" element={<Navigate to="/app" replace/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/app" element={<AppPage/>}/>
                <Route path="*" element={<Navigate to="/app" replace/>}/>
            </Routes>
        </BrowserRouter>
    )
}
