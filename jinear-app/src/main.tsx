import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {Provider} from 'react-redux'
import {PersistGate} from 'redux-persist/integration/react'
import {persistor, store} from '@/store'
import App from './App.tsx'
import './styles/app.scss'
import Scripts from "@/components/scripts/Scripts.tsx";
import Root from "@/components/root/Root.tsx";
import OnInstallPromptEventProvider from "@/components/onInstallPromptEventProvider/OnInstallPromptEventProvider.tsx";
import ThemeProvider from "@/components/themeProvider/ThemeProvider.tsx";
import posthog from 'posthog-js';
import {PostHogErrorBoundary, PostHogProvider} from '@posthog/react'

const POSTHOG_KEY = import.meta.env.VITE_POSTHOG_KEY;
const POSTHOG_HOST = import.meta.env.VITE_POSTHOG_HOST;
if (POSTHOG_KEY && POSTHOG_HOST) {
    posthog.init(POSTHOG_KEY, {
        api_host: POSTHOG_HOST,
        person_profiles: "identified_only",
        capture_pageview: false,
        capture_pageleave: true
    });
}

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <PostHogProvider client={posthog}>
            <PostHogErrorBoundary>
                <Provider store={store}>
                    <PersistGate loading={null} persistor={persistor}>
                        <OnInstallPromptEventProvider>
                            <Scripts/>
                            <ThemeProvider>
                                <Root>
                                    <App/>
                                </Root>
                            </ThemeProvider>
                        </OnInstallPromptEventProvider>
                    </PersistGate>
                </Provider>
            </PostHogErrorBoundary>
        </PostHogProvider>
    </StrictMode>,
)
