import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import {useAdminRetrieveWorkspaceTeamsQuery} from "@/store/api/adminTeamApi";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import AdminTeamRow from "./teamRow/AdminTeamRow";
import styles from "./AdminWorkspaceTeamsGroup.module.css";

interface AdminWorkspaceTeamsGroupProps {
    workspace: WorkspaceDto;
}

const AdminWorkspaceTeamsGroup: React.FC<AdminWorkspaceTeamsGroupProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {data: response, isFetching} = useAdminRetrieveWorkspaceTeamsQuery({workspaceId: workspace.workspaceId});

    return (
        <div className={styles.container}>
            <h3 className={styles.groupTitle}>{`${workspace.title} ${t("adminTeamsGroupTitleSuffix")}`}</h3>
            {response && response.data.length > 0 && (
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                        <tr>
                            <th className={styles.nameHeader}>{t("adminTableHeaderName")}</th>
                            <th className={styles.tagHeader}>{t("adminTableHeaderTag")}</th>
                            <th className={styles.actionsHeader}>{t("adminTableHeaderActions")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {response.data.map((team) => (
                            <AdminTeamRow
                                key={`admin-team-row-${team.teamId}`}
                                team={team}
                            />
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            {response && response.data.length == 0 && !isFetching && (
                <div className={styles.emptyStateContainer}>
                    <div className={styles.emptyLabel}>{t("adminTeamsGroupEmptyLabel")}</div>
                </div>
            )}
            {isFetching && !response && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </div>
    );
};

export default AdminWorkspaceTeamsGroup;
