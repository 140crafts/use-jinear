import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {
    useRetrieveMcpConnectionsQuery,
    useRetrieveMcpServerInfoQuery,
    useRevokeMcpConnectionMutation,
} from "@/store/api/mcpApi";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import {calculateDateDiff} from "@/util/DateHelper";
import Logger from "@/util/logger";
import React, {useEffect, useState} from "react";
import toast from "react-hot-toast";
import {LuCheck, LuCopy} from "react-icons/lu";
import styles from "./McpConnectionsSection.module.css";

interface McpConnectionsSectionProps {
    title?: string;
}

const logger = Logger("McpConnectionsSection");

/**
 * Everything a member needs to connect an AI assistant to this instance, and the list of
 * assistants they already connected.
 * <p>
 * The server URL is shown rather than left for the member to work out. It has to be typed
 * into the client exactly, path included, and on a self hosted instance nobody but the
 * server knows what it is.
 * <p>
 * The whole section hides itself when the instance says MCP is off, so a member never sees
 * setup steps for something they cannot connect to.
 */
const McpConnectionsSection: React.FC<McpConnectionsSectionProps> = ({title}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [copied, setCopied] = useState<boolean>(false);

    const {currentData: serverInfoResponse} = useRetrieveMcpServerInfoQuery();
    const enabled = serverInfoResponse?.data?.enabled == true;
    const serverUrl = serverInfoResponse?.data?.serverUrl;
    const documentationUrl = serverInfoResponse?.data?.documentationUrl;

    const {currentData: connectionsResponse} = useRetrieveMcpConnectionsQuery(undefined, {skip: !enabled});
    const [revokeConnection, {isLoading: isRevokeLoading}] = useRevokeMcpConnectionMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isRevokeLoading}));
    }, [isRevokeLoading]);

    useEffect(() => {
        if (!copied) {
            return;
        }
        const timeout = setTimeout(() => setCopied(false), 2000);
        return () => clearTimeout(timeout);
    }, [copied]);

    if (!enabled || !serverUrl) {
        return null;
    }

    const copyServerUrl = async () => {
        try {
            await navigator.clipboard.writeText(serverUrl);
            setCopied(true);
        } catch (error) {
            logger.log({copyFailed: error});
            toast(t("mcpConnectionsCopyFailed"));
        }
    };

    const disconnect = (mcpConnectionId: string) => () => {
        revokeConnection({mcpConnectionId});
    };

    const connections = connectionsResponse?.data ?? [];

    return (
        <div className={styles.container}>
            {title && <h2>{title}</h2>}
            <span className={styles.text}>{t("mcpConnectionsIntro")}</span>

            <div className={styles.urlBlock}>
                <div className={styles.urlLabel}>{t("mcpConnectionsServerUrlLabel")}</div>
                <div className={styles.urlRow}>
                    <code className={styles.url}>{serverUrl}</code>
                    <Button
                        heightVariant={ButtonHeight.short}
                        variant={ButtonVariants.filled}
                        onClick={copyServerUrl}
                    >
                        {copied ? <LuCheck className={styles.buttonIcon}/> : <LuCopy className={styles.buttonIcon}/>}
                        <span>{copied ? t("mcpConnectionsCopiedLabel") : t("mcpConnectionsCopyLabel")}</span>
                    </Button>
                </div>
            </div>

            <div className={styles.steps}>
                <div>
                    <h3 className={styles.stepTitle}>{t("mcpConnectionsClaudeStepsTitle")}</h3>
                    <ol className={styles.stepList}>
                        <li>{t("mcpConnectionsClaudeStep1")}</li>
                        <li>{t("mcpConnectionsClaudeStep2")}</li>
                        <li>{t("mcpConnectionsClaudeStep3")}</li>
                    </ol>
                </div>
                <div>
                    <h3 className={styles.stepTitle}>{t("mcpConnectionsChatgptStepsTitle")}</h3>
                    <ol className={styles.stepList}>
                        <li>{t("mcpConnectionsChatgptStep1")}</li>
                        <li>{t("mcpConnectionsChatgptStep2")}</li>
                        <li>{t("mcpConnectionsChatgptStep3")}</li>
                    </ol>
                </div>
            </div>

            <div className={styles.note}>{t("mcpConnectionsPrivateNetworkNote")}</div>
            {documentationUrl && (
                <a className={styles.docsLink} href={documentationUrl} target="_blank" rel="noreferrer noopener">
                    {t("mcpConnectionsDocsLink")}
                </a>
            )}

            <h3 className={styles.stepTitle}>{t("mcpConnectionsListTitle")}</h3>
            {connections.length == 0 && <span className={styles.text}>{t("mcpConnectionsListEmpty")}</span>}
            <div className={styles.connectionList}>
                {connections.map((connection) => (
                    <div key={connection.mcpConnectionId} className={styles.connectionRow}>
                        <div className={styles.connectionText}>
                            <div className={styles.connectionHost}>{connection.clientDisplayHost}</div>
                            {connection.clientName && (
                                <div className={styles.connectionName}>{connection.clientName}</div>
                            )}
                            <div className={styles.connectionMeta}>
                                {connection.lastUsedAt
                                    ? `${t("mcpConnectionsLastUsedLabel")} ${calculateDateDiff(connection.lastUsedAt)}`
                                    : t("mcpConnectionsNeverUsedLabel")}
                            </div>
                        </div>
                        <Button
                            heightVariant={ButtonHeight.short}
                            variant={ButtonVariants.outline}
                            disabled={isRevokeLoading}
                            onClick={disconnect(connection.mcpConnectionId)}
                        >
                            {t("mcpConnectionsDisconnectButton")}
                        </Button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default McpConnectionsSection;
