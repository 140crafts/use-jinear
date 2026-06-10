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

    // Sync active tab when the URL ?tab= param changes (back/forward, deep link)
    useEffect(() => {
        const fromUrl = searchParams.get("tab");
        const tab = tabs.find((t) => t.name == fromUrl);
        if (tab && tab.name != activeTab?.name) {
            setActiveTab(tab);
        }
    }, [searchParams, tabs, activeTab?.name]);

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
                    prev.set("tab", tab.name);
                    return prev;
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