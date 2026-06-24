import React from "react";
import type {TabViewProps} from "../tabView/TabView";
import NavigatorButton from "./navigatorButton/NavigatorButton";
import styles from "./TabNavigator.module.css";

interface TabNavigatorProps {
    tabs: TabViewProps[];
}

const TabNavigator: React.FC<TabNavigatorProps> = ({tabs}) => {
    return (
        <div className={styles.container}>
            {tabs?.map((tab) => (
                <NavigatorButton key={tab.name} {...tab}/>
            ))}
        </div>
    );
};

export default TabNavigator;
