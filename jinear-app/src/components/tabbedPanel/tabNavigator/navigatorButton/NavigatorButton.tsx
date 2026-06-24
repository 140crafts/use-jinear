import Button from "@/components/button";
import React, {type ReactElement} from "react";
import {useActiveTab, useChangeTab} from "../../context/TabContext";
import styles from "./NavigatorButton.module.css";
import SelectedNavBottomBar from "./selectedNavBottomBar/SelectedNavBottomBar";
import type {IconType} from "react-icons/lib";
import cn from "classnames";

interface NavigatorButtonProps {
    name: string;
    label: string;
    icon?: ReactElement;
    buttonClass?: string
}

const NavigatorButton: React.FC<NavigatorButtonProps> = ({name, label, icon, buttonClass}) => {
    const activeTab = useActiveTab();
    const changeTab = useChangeTab();
    const isActive = activeTab?.name == name;

    const onClick = () => {
        changeTab?.(name);
    };

    return (
        <div className={styles.container}>
            {isActive && <SelectedNavBottomBar/>}
            <Button className={cn(styles.button, buttonClass)} onClick={onClick}>
                {icon}
                {label}</Button>
        </div>
    );
};

export default NavigatorButton;
