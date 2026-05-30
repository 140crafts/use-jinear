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

createRoot(document.getElementById('root')!).render(
    <StrictMode>
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
    </StrictMode>,
)
