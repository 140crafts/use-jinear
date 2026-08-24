import React from "react";
import styles from "./InstanceSettingsSideMenu.module.scss";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "@/locals/useTranslation";
import Button, {ButtonVariants} from "@/components/button";
import {LuSettings} from "react-icons/lu";
import {useLocation} from "react-router-dom";

interface InstanceSettingsSideMenuProps {
}

const InstanceSettingsSideMenu: React.FC<InstanceSettingsSideMenuProps> = ({}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();

    const generalPath = `/admin/instance-settings/general`;

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
            </div>
        </div>
    );
};

export default InstanceSettingsSideMenu;
