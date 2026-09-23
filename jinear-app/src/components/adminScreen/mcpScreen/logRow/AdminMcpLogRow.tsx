import type {McpToolCallLogDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import {format} from "date-fns";
import React from "react";
import styles from "./AdminMcpLogRow.module.css";

interface AdminMcpLogRowProps {
    entry: McpToolCallLogDto;
}

const AdminMcpLogRow: React.FC<AdminMcpLogRowProps> = ({entry}) => {
    const {t} = useTranslation();

    return (
        <tr className={styles.row}>
            <td>
                <code className={styles.mono}>{entry.toolName}</code>
            </td>
            <td className={entry.callStatus == "OK" ? styles.statusOk : styles.statusBad}>{entry.callStatus}</td>
            <td className={styles.metaCell}>{entry.durationMs != null && `${entry.durationMs} ms`}</td>
            <td className={styles.metaCell}>
                {entry.createdDate && format(new Date(entry.createdDate), t("dateTimeFormat"))}
            </td>
        </tr>
    );
};

export default AdminMcpLogRow;
