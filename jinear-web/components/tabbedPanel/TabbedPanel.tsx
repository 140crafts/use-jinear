import Logger from "@/utils/logger";
import cn from "classnames";
import { AnimatePresence } from "framer-motion";
import React, { useEffect, useState } from "react";
import TabContext from "./context/TabContext";
import styles from "./TabbedPanel.module.css";
import TabNavigator from "./tabNavigator/TabNavigator";
import { TabViewProps } from "./tabView/TabView";
interface TabbedPanelProps {
  initialTabName?: string;
  children: React.ReactNode;
  containerClassName?: string;
}

const logger = Logger("TabbedPanel");

const retrieveNames = (children: React.ReactNode) => {
  const props = React.Children.map(children, (element) => {
    if (!React.isValidElement(element)) return;
    return { ...element.props, children: undefined };
  });
  logger.log({ retrieveNames: props });
  return props;
};

const TabbedPanel: React.FC<TabbedPanelProps> = ({ initialTabName, children, containerClassName }) => {
  const tabs: TabViewProps[] = retrieveNames(children) as TabViewProps[];
  const [activeTab, setActiveTab] = useState<TabViewProps>(tabs?.[0]);

  useEffect(() => {
    if (initialTabName) {
      changeTab(initialTabName);
    }
  }, [initialTabName]);
  logger.log({ tabs, activeTab, initialTabName });
  const changeTab = (tabName: string) => {
    const tab = tabs?.find((tab) => tab.name == tabName);
    if (tab) {
      setActiveTab(tab);
    }
  };

  return (
    <div className={cn(styles.container, containerClassName)}>
      <TabContext.Provider
        value={{
          activeTab,
          changeTab,
        }}
      >
        <TabNavigator tabs={tabs} />
        <AnimatePresence initial={false}>
          <div className={styles.tabViewContainer}>{children}</div>
        </AnimatePresence>
      </TabContext.Provider>
    </div>
  );
};

export default TabbedPanel;
