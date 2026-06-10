import CalendarMemberList from "@/components/calendarMembersPage/calendarMemberList/CalendarMemberList";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import styles from "./page.module.css";
import {useParams} from "react-router-dom";

interface CalendarMembersPageProps {
}

const CalendarMembersPage: React.FC<CalendarMembersPageProps> = ({}) => {
    const {workspaceName, calendarId} = useParams();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
    return (
        <div className={styles.container}>
            {workspace && calendarId &&
                <CalendarMemberList calendarId={calendarId} workspaceId={workspace.workspaceId}/>}
        </div>
    );
};

export default CalendarMembersPage;
