import cn from "classnames";
import React, {type ReactElement, useEffect, useState} from "react";
import {useActiveTab} from "../context/TabContext";
import styles from "./TabView.module.css";
import type {IconType} from "react-icons/lib";

export interface TabViewProps {
    name: string;
    label: string;
    children?: React.ReactNode;
    containerClassName?: string;
    containerRef?: React.Ref<HTMLDivElement>;
    icon?: ReactElement;
    buttonClass?: string
}

const TabView: React.FC<TabViewProps> = ({name, label, icon, children, containerClassName, containerRef}) => {
    const activeTab = useActiveTab();
    const isActive = name == activeTab?.name;
    const [hasMounted, setHasMounted] = useState(isActive);

    useEffect(() => {
        if (isActive) setHasMounted(true);
    }, [isActive]);

    return (!hasMounted) ? null : (
        <div
            ref={containerRef}
            className={cn(styles.container, isActive && styles.active, containerClassName)}
        >
            {children}
        </div>
    );
};

export default TabView;
