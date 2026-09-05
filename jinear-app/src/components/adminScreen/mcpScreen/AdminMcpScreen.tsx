import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Pagination from "@/components/pagination/Pagination";
import useTranslation from "@/locales/useTranslation";
import {useAdminRetrieveMcpAnalyticsQuery, useAdminRetrieveMcpLogsQuery} from "@/store/api/adminMcpApi";
import {useAdminRetrieveOauthClientsQuery, useAdminRevokeOauthClientMutation} from "@/store/api/adminOauthApi";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import {calculateDateDiff} from "@/util/DateHelper";
import React, {useEffect, useState} from "react";
import styles from "./AdminMcpScreen.module.css";

/**
 * The instance wide view of the MCP server: how much it is used, which client
 * applications registered themselves, and the recent call log.
 * <p>
 * Revoking a client here is heavier than a member disconnecting their own assistant. It
 * cuts every connection made through that client, for every account, which is the control
 * an administrator needs when a client turns out to be untrustworthy.
 */
const AdminMcpScreen: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
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
    const [revokeClient, {isLoading: isRevokeLoading}] = useAdminRevokeOauthClientMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isRevokeLoading}));
    }, [isRevokeLoading]);

    const analytics = analyticsResponse?.data;

    const revoke = (clientId: string) => () => {
        revokeClient({clientId});
    };

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
            {(analytics?.topTools?.length ?? 0) == 0 && (
                <span className={styles.text}>{t("adminMcpTopToolsEmpty")}</span>
            )}
            <div className={styles.rowList}>
                {analytics?.topTools?.map((tool) => (
                    <div key={tool.toolName} className={styles.row}>
                        <code className={styles.mono}>{tool.toolName}</code>
                        <div className={styles.rowMeta}>
                            <span>{`${t("adminMcpToolCallsLabel")} ${tool.callCount}`}</span>
                            <span>{`${t("adminMcpToolErrorsLabel")} ${tool.errorCount}`}</span>
                            <span>{`${t("adminMcpToolAverageLabel")} ${tool.averageDurationMs} ms`}</span>
                        </div>
                    </div>
                ))}
            </div>

            <h3>{t("adminMcpClientsTitle")}</h3>
            <span className={styles.text}>{t("adminMcpClientsText")}</span>
            {clientsResponse && (
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
            )}
            <div className={styles.rowList}>
                {clientsResponse?.data.content.map((client) => (
                    <div key={client.clientId} className={styles.row}>
                        <div className={styles.rowText}>
                            <div className={styles.rowTitle}>{client.clientName || client.clientId}</div>
                            <code className={styles.mono}>{client.clientId}</code>
                            <div className={styles.rowMeta}>
                                <span>{client.registrationType}</span>
                                {client.clientIdIssuedAt && (
                                    <span>{`${t("adminMcpClientRegisteredLabel")} ${calculateDateDiff(client.clientIdIssuedAt)}`}</span>
                                )}
                            </div>
                        </div>
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.outline}
                            disabled={isRevokeLoading}
                            onClick={revoke(client.clientId)}
                        >
                            {t("adminMcpClientRevokeButton")}
                        </Button>
                    </div>
                ))}
                {clientsResponse && !clientsResponse.data.hasContent && !isClientsFetching && (
                    <span className={styles.text}>{t("adminMcpClientsEmpty")}</span>
                )}
                {isClientsFetching && !clientsResponse && <CircularLoading/>}
            </div>

            <h3>{t("adminMcpLogTitle")}</h3>
            {logsResponse && (
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
            )}
            <div className={styles.rowList}>
                {logsResponse?.data.content.map((entry) => (
                    <div key={entry.mcpToolCallLogId} className={styles.row}>
                        <div className={styles.rowText}>
                            <code className={styles.mono}>{entry.toolName}</code>
                            <div className={styles.rowMeta}>
                                <span className={entry.callStatus == "OK" ? styles.statusOk : styles.statusBad}>
                                    {entry.callStatus}
                                </span>
                                {entry.durationMs != null && <span>{`${entry.durationMs} ms`}</span>}
                                {entry.createdDate && <span>{calculateDateDiff(entry.createdDate)}</span>}
                            </div>
                        </div>
                    </div>
                ))}
                {logsResponse && !logsResponse.data.hasContent && !isLogsFetching && (
                    <span className={styles.text}>{t("adminMcpLogEmpty")}</span>
                )}
                {isLogsFetching && !logsResponse && <CircularLoading/>}
            </div>
        </div>
    );
};

export default AdminMcpScreen;
