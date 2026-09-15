import type {McpToolUsageDto} from "@/model/be/jinear-core";
import React from "react";
import styles from "./AdminMcpToolRow.module.css";

interface AdminMcpToolRowProps {
    tool: McpToolUsageDto;
}

const AdminMcpToolRow: React.FC<AdminMcpToolRowProps> = ({tool}) => {
    return (
        <tr className={styles.row}>
            <td>
                <code className={styles.mono}>{tool.toolName}</code>
            </td>
            <td className={styles.metaCell}>{tool.callCount}</td>
            <td className={styles.metaCell}>{tool.errorCount}</td>
            <td className={styles.metaCell}>{`${tool.averageDurationMs} ms`}</td>
        </tr>
    );
};

export default AdminMcpToolRow;
