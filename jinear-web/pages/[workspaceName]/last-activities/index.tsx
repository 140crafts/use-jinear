import LastActivitiesList from "@/components/lastActivitiesScreen/LastActivitiesList";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface LastActivitiesScreenProps {}

const LastActivitiesScreen: React.FC<LastActivitiesScreenProps> = ({}) => {
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  return <div className={styles.container}>{currentWorkspace && <LastActivitiesList workspace={currentWorkspace} />}</div>;
};

export default LastActivitiesScreen;
