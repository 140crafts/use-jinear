import AdminWorkspacesScreen from "@/components/adminScreen/workspacesScreen/AdminWorkspacesScreen";
import React from "react";
import styles from "./page.module.css";

const AdminWorkspacesPage: React.FC = () => {
    return (
        <div className={styles.container}>
            <AdminWorkspacesScreen/>
            <div className="spacer-h-4"/>
        </div>
    );
};

export default AdminWorkspacesPage;
