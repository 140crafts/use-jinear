import React from "react";
import styles from "./InstanceSettingsSideMenu.module.scss";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "@/locals/useTranslation";
import Button, {ButtonVariants} from "@/components/button";
import {LuBot, LuBuilding2, LuSettings, LuUserCog, LuUsers} from "react-icons/lu";
import {useLocation} from "react-router-dom";

interface InstanceSettingsSideMenuProps {
}

const InstanceSettingsSideMenu: React.FC<InstanceSettingsSideMenuProps> = ({}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();

    const generalPath = `/admin/instance-settings/general`;
    const workspacesPath = `/admin/workspaces`;
    const teamsPath = `/admin/teams`;
    const accountsPath = `/admin/accounts`;
    const mcpPath = `/admin/mcp`;

    return (
        <div className={styles.container}>
            <MenuGroupTitle label={t("instanceSettingsSideMenuTitle")}/>
            <div className={styles.buttonsContainer}>
                <Button
                    className={styles.button}
                    href={generalPath}
                    variant={pathname?.indexOf(generalPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuSettings className={"icon"}/>
                    {t("instanceSettingsSideMenuGeneral")}
                </Button>
                <Button
                    className={styles.button}
                    href={workspacesPath}
                    variant={pathname?.indexOf(workspacesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuBuilding2 className={"icon"}/>
                    {t("instanceSettingsSideMenuWorkspaces")}
                </Button>
                <Button
                    className={styles.button}
                    href={teamsPath}
                    variant={pathname?.indexOf(teamsPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuUsers className={"icon"}/>
                    {t("instanceSettingsSideMenuTeams")}
                </Button>
                <Button
                    className={styles.button}
                    href={accountsPath}
                    variant={pathname?.indexOf(accountsPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuUserCog className={"icon"}/>
                    {t("instanceSettingsSideMenuAccounts")}
                </Button>
                <Button
                    className={styles.button}
                    href={mcpPath}
                    variant={pathname?.indexOf(mcpPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuBot className={"icon"}/>
                    {t("instanceSettingsSideMenuMcp")}
                </Button>
            </div>
        </div>
    );
};

export default InstanceSettingsSideMenu;
