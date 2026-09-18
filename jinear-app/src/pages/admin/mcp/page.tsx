import AdminMcpScreen from "@/components/adminScreen/mcpScreen/AdminMcpScreen";
import React from "react";
import styles from "./page.module.css";

const AdminMcpPage: React.FC = () => {
    return (
        <div className={styles.container}>
            <AdminMcpScreen/>
            <div className="spacer-h-4"/>
        </div>
    );
};

export default AdminMcpPage;
