import Button from "@/components/button";
import React from "react";
import {useActiveTab, useChangeTab} from "../../context/TabContext";
import styles from "./NavigatorButton.module.css";
import SelectedNavBottomBar from "./selectedNavBottomBar/SelectedNavBottomBar";

interface NavigatorButtonProps {
    name: string;
    label: string;
}

const NavigatorButton: React.FC<NavigatorButtonProps> = ({name, label}) => {
    const activeTab = useActiveTab();
    const changeTab = useChangeTab();
    const isActive = activeTab?.name == name;

    const onClick = () => {
        changeTab?.(name);
    };

    return (
        <div className={styles.container}>
            {isActive && <SelectedNavBottomBar/>}
            <Button className={styles.button} onClick={onClick}>{label}</Button>
        </div>
    );
};

export default NavigatorButton;
