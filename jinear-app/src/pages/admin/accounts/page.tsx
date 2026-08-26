import AdminAccountsScreen from "@/components/adminScreen/accountsScreen/AdminAccountsScreen";
import React from "react";
import styles from "./page.module.css";

const AdminAccountsPage: React.FC = () => {
    return (
        <div className={styles.container}>
            <AdminAccountsScreen/>
            <div className="spacer-h-4"/>
        </div>
    );
};

export default AdminAccountsPage;
