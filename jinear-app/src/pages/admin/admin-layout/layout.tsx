import React from "react";
import styles from "./layout.module.scss";
import cn from "classnames";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";
import InstanceSettingsSideMenu from "@/components/instanceSettingsSideMenu/InstanceSettingsSideMenu";
import useTranslation from "@/locals/useTranslation.ts";
import {selectAuthState} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import useIsInstanceAdmin from "@/hooks/useIsInstanceAdmin";
import {LuArrowLeft, LuShieldCheck} from "react-icons/lu";
import {Navigate, Outlet} from "react-router-dom";

interface AdminLayoutProps {

}

const AdminLayout: React.FC<AdminLayoutProps> = ({}) => {
    const {t} = useTranslation();
    const authState = useTypedSelector(selectAuthState);
    const isInstanceAdmin = useIsInstanceAdmin();

    // AuthCheck owns the not logged in redirect. Until the account lands the roles are not
    // known yet, so hold instead of bouncing an admin who opened this url directly.
    if (authState == "NOT_DECIDED") {
        return null;
    }

    if (!isInstanceAdmin) {
        return <Navigate to="/" replace/>;
    }

    return (
        <div id="admin-layout-container" className={styles.container}>
            <div id="admin-layout-header" className={styles.header}>
                {/* AppPage routes a logged in account on to its preferred workspace, so "/"
                    is the whole way back and this shell never needs a workspace of its own. */}
                <Button
                    className={styles.backButton}
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.hoverFilled}
                    href={"/"}
                >
                    <LuArrowLeft className={"icon"}/>
                    <span className={styles.backButtonLabel}>{t("adminLayoutBackToApp")}</span>
                </Button>

                <b className={styles.title}>{t("instanceSettingsSideMenuTitle")}</b>

                <div className={styles.headerRightContent}>
                    <ThemeToggle variant={ButtonVariants.hoverFilled}/>
                </div>
            </div>

            <div id="admin-layout-content" className={styles.content}>
                <SecondLevelSideMenuV2
                    mobileFabButtonIcon={<LuShieldCheck className={"icon"} size={18}/>}
                    mobileFabButtonText={t('tasksLayoutSideMenuCollapsedLabel')}
                >
                    <InstanceSettingsSideMenu/>
                </SecondLevelSideMenuV2>
                <div
                    id="admin-layout-page-content"
                    className={cn(styles.pageContent, styles.pageContentWithSideMenu)}
                >
                    <Outlet/>
                </div>
            </div>
        </div>
    );
};

export default AdminLayout;
