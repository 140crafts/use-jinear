import GradientLine from "@/components/gradientLine/GradientLine";
import React from "react";
import styles from "./TaskSectionHeader.module.css";

interface TaskSectionHeaderProps {
    title: string;
    children?: React.ReactNode;
}

const TaskSectionHeader: React.FC<TaskSectionHeaderProps> = ({title, children}) => {
    return (
        <div className={styles.container}>
            <div className={styles.row}>
                <h3>{title}</h3>
                {children && <div className={styles.actions}>{children}</div>}
            </div>
            <GradientLine/>
        </div>
    );
};

export default TaskSectionHeader;
