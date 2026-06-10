import { createContext, useContext } from "react";
import { TabViewProps } from "../tabView/TabView";

interface ITabContext {
  activeTab?: TabViewProps;
  changeTab?: (tabName: string) => void;
}

const TabContext = createContext<ITabContext>({});

export default TabContext;

export function useActiveTab() {
  const ctx = useContext(TabContext);
  return ctx.activeTab;
}

export function useChangeTab() {
  const ctx = useContext(TabContext);
  return ctx.changeTab;
}
