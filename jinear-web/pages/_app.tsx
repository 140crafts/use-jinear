import AuthCheck from "@/components/authCheck/AuthCheck";
import ErrorBoundary from "@/components/errorBoundary/ErrorBoundary";
import LayoutProvider from "@/components/layoutProvider/LayoutProvider";
import MainHeader from "@/components/mainHeader/MainHeader";
import AddMemberToTeamModal from "@/components/modal/addMemberToTeamModal/AddMemberToTeamModal";
import DatePickerModal from "@/components/modal/datePicker/DatePickerModal";
import DialogModal from "@/components/modal/dialogModal/DialogModal";
import LoadingModal from "@/components/modal/loadingModal/LoadingModal";
import LoginWith2FaMailModal from "@/components/modal/loginWith2FaMailModal/LoginWith2FaMailModal";
import NewTaskModal from "@/components/modal/newTaskModal/NewTaskModal";
import NewTeamModal from "@/components/modal/newTeamModal/NewTeamModal";
import NewWorkspaceModal from "@/components/modal/newWorkspaceModal/NewWorkspaceModal";
import NotFoundModal from "@/components/modal/notFoundModal/NotFoundModal";
import NewReminderModal from "@/components/modal/reminder/NewReminderModal/NewReminderModal";
import ReminderListModal from "@/components/modal/reminder/reminderListModal/ReminderListModal";
import SearchTaskModal from "@/components/modal/searchTaskModal/SearchTaskModal";
import TaskAssigneeChangeModal from "@/components/modal/taskDetailModals/taskAssigneeChangeModal/TaskAssigneeChangeModal";
import TaskDateChangeModal from "@/components/modal/taskDetailModals/taskDateChangeModal/TaskDateChangeModal";
import TaskTopicChangeModal from "@/components/modal/taskDetailModals/taskTopicChangeModal/TaskTopicChangeModal";
import WorkflowChangeStatusModal from "@/components/modal/taskDetailModals/workflowChangeStatus/WorkflowChangeStatusModal";
import TeamOptionsModal from "@/components/modal/teamOptionsModal/TeamOptionsModal";
import WorkspaceInviteMemberModal from "@/components/modal/workspaceInviteMemberModal/WorkspaceInviteMemberModal";
import OnboardListener from "@/components/onboardListener/OnboardListener";
import TitleHandler from "@/components/titleHandler/TitleHandler";
import Transition from "@/components/transition/Transition";
import WorkspaceAndTeamChangeListener from "@/components/workspaceAndTeamChangeListener/WorkspaceAndTeamChangeListener";
import ThemeContext, { getTheme } from "@/store/context/themeContext";
import { store } from "@/store/store";
import enUsLocale from "date-fns/locale/en-US";
import trTrLocale from "date-fns/locale/tr";
import setDefaultOptions from "date-fns/setDefaultOptions";
import useTranslation from "locales/useTranslation";
import { AppProps } from "next/app";
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
    <ErrorBoundary message={"AddMemberToTeamModal"}>
      <AddMemberToTeamModal />
    </ErrorBoundary>
    <ErrorBoundary message={"WorkspaceInviteMemberModal"}>
      <WorkspaceInviteMemberModal />
    </ErrorBoundary>
    <ErrorBoundary message={"ReminderListModal"}>
      <ReminderListModal />
    </ErrorBoundary>
    <ErrorBoundary message={"NewReminderModal"}>
      <NewReminderModal />
    </ErrorBoundary>
    <ErrorBoundary message={"SearchTaskModal"}>
      <SearchTaskModal />
    </ErrorBoundary>
    <ErrorBoundary message={"NewTeamModal"}>
      <NewTeamModal />
    </ErrorBoundary>
    <ErrorBoundary message={"NewWorkspaceModal"}>
      <NewWorkspaceModal />
    </ErrorBoundary>
    <ErrorBoundary message={"TaskAssigneeChangeModal"}>
      <TaskAssigneeChangeModal />
    </ErrorBoundary>
    <ErrorBoundary message={"TaskDateChangeModal"}>
      <TaskDateChangeModal />
    </ErrorBoundary>
    <ErrorBoundary message={"TaskTopicChangeModal"}>
      <TaskTopicChangeModal />
    </ErrorBoundary>
    <ErrorBoundary message={"WorkflowChangeStatusModal"}>
      <WorkflowChangeStatusModal />
    </ErrorBoundary>
    <ErrorBoundary message={"TeamOptionsModal"}>
      <TeamOptionsModal />
    </ErrorBoundary>
    <ErrorBoundary message={"NewTaskModal"}>
      <NewTaskModal />
    </ErrorBoundary>
    <ErrorBoundary message={"LoginWith2FaMailModal"}>
      <LoginWith2FaMailModal />
    </ErrorBoundary>
    <ErrorBoundary message={"DatePickerModal"}>
      <DatePickerModal />
    </ErrorBoundary>
    <ErrorBoundary message={"DialogModal"}>
      <DialogModal />
    </ErrorBoundary>
    <ErrorBoundary message={"NotFoundModal"}>
      <NotFoundModal />
    </ErrorBoundary>
    <ErrorBoundary message={"LoadingModal"}>
      <LoadingModal />
    </ErrorBoundary>
  </>
);

function MyApp({ Component, pageProps }: AppProps) {
  const { t } = useTranslation();
  const [theme, setTheme] = useState(getTheme());

  const dateFnsLocale = t("localeType") == "TR" ? trTrLocale : enUsLocale;
  setDefaultOptions({ locale: dateFnsLocale });

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
      <MainHeader />
      <Provider store={store}>
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
          <LayoutProvider>
            <TitleHandler />
            <AuthCheck />
            <WorkspaceAndTeamChangeListener />
            <OnboardListener />
            <Transition>
              <Component {...pageProps} />
            </Transition>
            <Toaster position="bottom-center" containerStyle={{ bottom: 68 }} toastOptions={{ className: "toast" }} />
            <ErrorBoundary message={"Global Modals"}>{globalModals}</ErrorBoundary>
          </LayoutProvider>
        </ThemeContext.Provider>
      </Provider>
    </>
  );
}

export default MyApp;
