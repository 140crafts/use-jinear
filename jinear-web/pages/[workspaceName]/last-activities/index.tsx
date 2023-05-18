import LastActivitiesList from "@/components/lastActivitiesScreen/LastActivitiesList";
import LastActivitiesBreadCrumb from "@/components/lastActivitiesScreen/lastActivitiesScreenHeader/breadcrumb/LastActivitiesBreadCrumb";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface LastActivitiesScreenProps {}

const LastActivitiesScreen: React.FC<LastActivitiesScreenProps> = ({}) => {
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  return (
    <div className={styles.container}>
      <LastActivitiesBreadCrumb />
      {currentWorkspace && <LastActivitiesList workspace={currentWorkspace} />}
    </div>
  );
};

export default LastActivitiesScreen;
