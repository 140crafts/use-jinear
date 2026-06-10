import NewWorkspaceForm from "@/components/form/newWorkspaceForm/NewWorkspaceForm";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./page.module.css";
import {useNavigate} from "react-router-dom";

interface NewWorkspaceScreenProps {
}

const NewWorkspaceScreen: React.FC<NewWorkspaceScreenProps> = ({}) => {
    const navigate = useNavigate();
    const {t} = useTranslation();

    const routeHome = () => {
        navigate(ROUTE_IF_LOGGED_IN, {replace: true});
    };
    return (
        <div className={styles.container}>
            <div className={styles.headerContainer}>
                <div className={styles.header}>{t("newWorkspaceScreenTitle")}</div>
                <div className="flex-1"/>
                <ThemeToggle/>
            </div>
            <div className={styles.text}>{t("newWorkspaceScreenText")}</div>
            <div className={styles.subText}>{t("newWorkspaceScreenSubtext")}</div>
            <div className="spacer-h-2"/>
            <div className={styles.formContainer}>
                <NewWorkspaceForm onSuccess={routeHome}/>
            </div>
        </div>
    );
};

export default NewWorkspaceScreen;
