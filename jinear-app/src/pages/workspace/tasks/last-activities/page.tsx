import LastActivitiesList from "@/components/lastActivitiesScreen/LastActivitiesList";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import styles from "./index.module.css";
import {useParams} from "react-router-dom";

interface LastActivitiesScreenProps {
}

const LastActivitiesScreen: React.FC<LastActivitiesScreenProps> = ({}) => {
    const params = useParams();
    const workspaceName: string = params?.workspaceName as string;
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    return (
        <div className={styles.container}>
            {workspace && <LastActivitiesList workspace={workspace}/>}
        </div>
    );
};

export default LastActivitiesScreen;
