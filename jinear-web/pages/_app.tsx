import AuthCheck from "@/components/authCheck/AuthCheck";
import LayoutProvider from "@/components/layoutProvider/LayoutProvider";
import LoadingModal from "@/components/modal/loadingModal/LoadingModal";
import LoginWith2FaMailModal from "@/components/modal/loginWith2FaMailModal/LoginWith2FaMailModal";
import NewTaskModal from "@/components/modal/newTaskModal/NewTaskModal";
import NewWorkspaceModal from "@/components/modal/newWorkspaceModal/NewWorkspaceModal";
import NotFoundModal from "@/components/modal/notFoundModal/NotFoundModal";
import TaskAssigneeChangeModal from "@/components/modal/taskDetailModals/taskAssigneeChangeModal/TaskAssigneeChangeModal";
import TaskDateChangeModal from "@/components/modal/taskDetailModals/taskDateChangeModal/TaskDateChangeModal";
import TaskTopicChangeModal from "@/components/modal/taskDetailModals/taskTopicChangeModal/TaskTopicChangeModal";
import WorkflowChangeStatusModal from "@/components/modal/taskDetailModals/workflowChangeStatus/WorkflowChangeStatusModal";
import TeamOptionsModal from "@/components/modal/teamOptionsModal/TeamOptionsModal";
import TitleHandler from "@/components/titleHandler/TitleHandler";
import Transition from "@/components/transition/Transition";
import ThemeContext, { getTheme } from "@/store/context/themeContext";
import { store } from "@/store/store";
import { AppProps } from "next/app";
import Head from "next/head";
import { useContext, useEffect, useState } from "react";
import { Toaster } from "react-hot-toast";
import { Provider } from "react-redux";
import "../styles/app.css";

export function useTheme() {
  const theme = useContext(ThemeContext);
  return theme.theme;
}

export function useThemeToggle() {
  const theme = useContext(ThemeContext);
  return theme.toggleTheme;
}

const globalModals: any = (
  <>
    <NewWorkspaceModal />
    <TaskAssigneeChangeModal />
    <TaskDateChangeModal />
    <TaskTopicChangeModal />
    <WorkflowChangeStatusModal />
    <TeamOptionsModal />
    <NewTaskModal />
    <LoginWith2FaMailModal />
    <NotFoundModal />
    <LoadingModal />
  </>
);

function MyApp({ Component, pageProps }: AppProps) {
  const [theme, setTheme] = useState(getTheme());

  const toggleTheme = () => {
    const next = theme == "dark" ? "light" : "dark";
    setTheme(next);
  };

  useEffect(() => {
    const $html = document.querySelector("html");
    $html?.classList.remove("light");
    $html?.classList.remove("dark");
    $html?.classList.add(theme);
    localStorage.setItem("THEME", theme);
  }, [theme]);

  return (
    <>
      <Head>
        <meta charSet="utf-8" />
        <meta
          name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"
        />
        <link
          rel="icon"
          href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>🌊</text></svg>"
        />
      </Head>
      <Provider store={store}>
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
          <LayoutProvider>
            <TitleHandler />
            <AuthCheck />
            {/* <WorkspaceAndTeamChangeListener /> */}
            <Transition>
              <Component {...pageProps} />
            </Transition>
            <Toaster
              position="bottom-center"
              containerStyle={{ bottom: 68 }}
              toastOptions={{ className: "toast" }}
            />
            {globalModals}
          </LayoutProvider>
        </ThemeContext.Provider>
      </Provider>
    </>
  );
}

export default MyApp;
