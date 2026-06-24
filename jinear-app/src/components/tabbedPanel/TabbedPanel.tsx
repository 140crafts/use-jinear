import Logger from "@/util/logger";
import cn from "classnames";
import React, {useEffect, useMemo, useState} from "react";
import {useSearchParams} from "react-router-dom";
import TabContext from "./context/TabContext";
import styles from "./TabbedPanel.module.css";
import TabNavigator from "./tabNavigator/TabNavigator";
import type {TabViewProps} from "./tabView/TabView";

interface TabbedPanelProps {
    initialTabName?: string;
    children: React.ReactNode;
    containerClassName?: string;
}

const logger = Logger("TabbedPanel");

const retrieveNames = (children: React.ReactNode) => {
    const props = React.Children.map(children, (element) => {
        if (!React.isValidElement(element)) return;
        return {...(element.props as TabViewProps), children: undefined};
    });
    logger.log({retrieveNames: props});
    return props;
};

const TabbedPanel: React.FC<TabbedPanelProps> = ({initialTabName, children, containerClassName}) => {
    const tabs: TabViewProps[] = useMemo(
        () => (retrieveNames(children) ?? []) as TabViewProps[],
        [children]
    );
    const resolveInitial = (): TabViewProps | undefined => {
        const fromUrl = searchParams.get("tab");
        return (
            tabs.find((tab) => tab.name == fromUrl) ??
            tabs.find((tab) => tab.name == initialTabName) ??
            tabs[0]
        );
    };

    const [searchParams, setSearchParams] = useSearchParams();
    const [activeTab, setActiveTab] = useState<TabViewProps | undefined>(resolveInitial);

    // The active tab is owned by local state and changed only through changeTab
    // (user clicks). We intentionally do NOT sync it back from ?tab= on every
    // searchParams change: tab content (e.g. MultiViewTaskList) writes unrelated
    // query params via navigate(), and re-deriving the active tab from the URL on
    // that churn caused the tab to flicker/ping-pong. The initial ?tab= is still
    // honored once via resolveInitial for deep-linking.

    // Fallback if initialTabName changes and no URL param is set
    useEffect(() => {
        if (initialTabName && !searchParams.get("tab")) {
            changeTab(initialTabName);
        }
    }, [initialTabName]);

    logger.log({tabs, activeTab, initialTabName});

    const changeTab = (tabName: string) => {
        const tab = tabs.find((t) => t.name == tabName);
        if (tab) {
            setActiveTab(tab);
            setSearchParams(
                (prev) => {
                    const next = new URLSearchParams(prev);
                    next.set("tab", tab.name);
                    return next;
                },
                {replace: true}
            );
        }
    };

    return (
        <div className={cn(styles.container, containerClassName)}>
            <TabContext.Provider value={{activeTab, changeTab}}>
                <TabNavigator tabs={tabs}/>
                <div className={styles.tabViewContainer}>{children}</div>
            </TabContext.Provider>
        </div>
    );
};

export default TabbedPanel;