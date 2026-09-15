import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Pagination from "@/components/pagination/Pagination";
import useTranslation from "@/locales/useTranslation";
import {useAdminRetrieveMcpAnalyticsQuery, useAdminRetrieveMcpLogsQuery} from "@/store/api/adminMcpApi";
import {useAdminRetrieveOauthClientsQuery} from "@/store/api/adminOauthApi";
import React, {useState} from "react";
import AdminMcpClientRow from "./clientRow/AdminMcpClientRow";
import AdminMcpLogRow from "./logRow/AdminMcpLogRow";
import AdminMcpToolRow from "./toolRow/AdminMcpToolRow";
import styles from "./AdminMcpScreen.module.css";

const AdminMcpScreen: React.FC = () => {
    const {t} = useTranslation();
    const [clientPage, setClientPage] = useState<number>(0);
    const [logPage, setLogPage] = useState<number>(0);

    const {currentData: analyticsResponse} = useAdminRetrieveMcpAnalyticsQuery();
    const {
        currentData: clientsResponse,
        isFetching: isClientsFetching,
    } = useAdminRetrieveOauthClientsQuery({page: clientPage});
    const {
        currentData: logsResponse,
        isFetching: isLogsFetching,
    } = useAdminRetrieveMcpLogsQuery({page: logPage});

    const analytics = analyticsResponse?.data;
    const topTools = analytics?.topTools ?? [];

    return (
        <div className={styles.container}>
            <div className={styles.titleBar}>
                <h2>{t("adminMcpTitle")}</h2>
            </div>
            <span className={styles.text}>{t("adminMcpIntro")}</span>

            <div className={styles.statRow}>
                <div className={styles.stat}>
                    <div className={styles.statValue}>{analytics?.totalCalls ?? 0}</div>
                    <div className={styles.statLabel}>{t("adminMcpStatTotalCalls")}</div>
                </div>
                <div className={styles.stat}>
                    <div className={styles.statValue}>{analytics?.errorCalls ?? 0}</div>
                    <div className={styles.statLabel}>{t("adminMcpStatErrorCalls")}</div>
                </div>
                <div className={styles.stat}>
                    <div className={styles.statValue}>{analytics?.activeConnections ?? 0}</div>
                    <div className={styles.statLabel}>{t("adminMcpStatActiveConnections")}</div>
                </div>
                <div className={styles.stat}>
                    <div className={styles.statValue}>{analytics?.windowDays ?? 0}</div>
                    <div className={styles.statLabel}>{t("adminMcpStatWindowDays")}</div>
                </div>
            </div>

            <h3>{t("adminMcpTopToolsTitle")}</h3>
            <div className={styles.content}>
                {topTools.length != 0 && (
                    <div className={styles.tableWrapper}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th className={styles.primaryHeader}>{t("adminTableHeaderTool")}</th>
                                <th>{t("adminTableHeaderCalls")}</th>
                                <th>{t("adminTableHeaderErrors")}</th>
                                <th>{t("adminTableHeaderAverageDuration")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {topTools.map((tool) => (
                                <AdminMcpToolRow key={`admin-mcp-tool-row-${tool.toolName}`} tool={tool}/>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
                {topTools.length == 0 && (
                    <div className={styles.emptyStateContainer}>
                        <div className={styles.emptyLabel}>{t("adminMcpTopToolsEmpty")}</div>
                    </div>
                )}
            </div>

            <h3>{t("adminMcpClientsTitle")}</h3>
            <span className={styles.text}>{t("adminMcpClientsText")}</span>
            {clientsResponse && (
                <div className={styles.header}>
                    <Pagination
                        id={"admin-mcp-client-paginator"}
                        className={styles.pagination}
                        pageNumber={clientsResponse.data.number}
                        pageSize={clientsResponse.data.size}
                        totalPages={clientsResponse.data.totalPages}
                        totalElements={clientsResponse.data.totalElements}
                        hasPrevious={clientsResponse.data.hasPrevious}
                        hasNext={clientsResponse.data.hasNext}
                        isLoading={isClientsFetching}
                        page={clientPage}
                        setPage={setClientPage}
                    />
                </div>
            )}
            <div className={styles.content}>
                {clientsResponse?.data.hasContent && (
                    <div className={styles.tableWrapper}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th className={styles.primaryHeader}>{t("adminTableHeaderName")}</th>
                                <th>{t("adminTableHeaderClientId")}</th>
                                <th>{t("adminTableHeaderType")}</th>
                                <th>{t("adminTableHeaderRegistered")}</th>
                                <th className={styles.actionsHeader}>{t("adminTableHeaderActions")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {clientsResponse.data.content.map((client) => (
                                <AdminMcpClientRow key={`admin-mcp-client-row-${client.clientId}`} client={client}/>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
                {clientsResponse && !clientsResponse.data.hasContent && !isClientsFetching && (
                    <div className={styles.emptyStateContainer}>
                        <div className={styles.emptyLabel}>{t("adminMcpClientsEmpty")}</div>
                    </div>
                )}
                {isClientsFetching && !clientsResponse && <CircularLoading/>}
            </div>

            <h3>{t("adminMcpLogTitle")}</h3>
            {logsResponse && (
                <div className={styles.header}>
                    <Pagination
                        id={"admin-mcp-log-paginator"}
                        className={styles.pagination}
                        pageNumber={logsResponse.data.number}
                        pageSize={logsResponse.data.size}
                        totalPages={logsResponse.data.totalPages}
                        totalElements={logsResponse.data.totalElements}
                        hasPrevious={logsResponse.data.hasPrevious}
                        hasNext={logsResponse.data.hasNext}
                        isLoading={isLogsFetching}
                        page={logPage}
                        setPage={setLogPage}
                    />
                </div>
            )}
            <div className={styles.content}>
                {logsResponse?.data.hasContent && (
                    <div className={styles.tableWrapper}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th className={styles.primaryHeader}>{t("adminTableHeaderTool")}</th>
                                <th>{t("adminTableHeaderStatus")}</th>
                                <th>{t("adminTableHeaderDuration")}</th>
                                <th>{t("adminTableHeaderTime")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {logsResponse.data.content.map((entry) => (
                                <AdminMcpLogRow key={`admin-mcp-log-row-${entry.mcpToolCallLogId}`} entry={entry}/>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
                {logsResponse && !logsResponse.data.hasContent && !isLogsFetching && (
                    <div className={styles.emptyStateContainer}>
                        <div className={styles.emptyLabel}>{t("adminMcpLogEmpty")}</div>
                    </div>
                )}
                {isLogsFetching && !logsResponse && <CircularLoading/>}
            </div>
        </div>
    );
};

export default AdminMcpScreen;
