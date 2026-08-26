import AdminTeamsScreen from "@/components/adminScreen/teamsScreen/AdminTeamsScreen";
import React from "react";
import styles from "./page.module.css";

const AdminTeamsPage: React.FC = () => {
    return (
        <div className={styles.container}>
            <AdminTeamsScreen/>
            <div className="spacer-h-4"/>
        </div>
    );
};

export default AdminTeamsPage;
