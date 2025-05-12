import { AnimatePresence } from "framer-motion";
import React from "react";
import { TabViewProps } from "../tabView/TabView";
import NavigatorButton from "./navigatorButton/NavigatorButton";
import styles from "./TabNavigator.module.css";

interface TabNavigatorProps {
  tabs: TabViewProps[];
}

const TabNavigator: React.FC<TabNavigatorProps> = ({ tabs }) => {
  return (
    <div className={styles.container}>
      <AnimatePresence>
        {tabs?.map((tab) => (
          <NavigatorButton key={tab.name} name={tab.name} label={tab.label} />
        ))}
      </AnimatePresence>
    </div>
  );
};

export default TabNavigator;
