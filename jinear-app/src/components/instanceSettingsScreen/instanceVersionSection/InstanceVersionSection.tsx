import {useRetrieveInstanceInfoQuery} from "@/store/api/adminInstanceInfoApi";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./InstanceVersionSection.module.css";

const TELEMETRY_DOCS_URL = "https://github.com/140crafts/use-jinear/blob/main/docs/telemetry.md";
const UPDATE_COMMAND = "docker compose pull && docker compose up -d";

interface InfoRowProps {
    label: string;
    value: string;
}

const InfoRow: React.FC<InfoRowProps> = ({label, value}) => {
    return (
        <div className={styles.row}>
            <span className={styles.label}>{label}</span>
            <span className={styles.value}>{value}</span>
        </div>
    );
};

interface InstanceVersionSectionProps {
}

const InstanceVersionSection: React.FC<InstanceVersionSectionProps> = ({}) => {
    const {t} = useTranslation();
    const {currentData: instanceInfoResponse} = useRetrieveInstanceInfoQuery();
    const instanceStatus = instanceInfoResponse?.data;

    if (!instanceStatus) {
        return null;
    }

    const statusText = (enabled: boolean) => enabled ? t("instanceVersionStatusOn") : t("instanceVersionStatusOff");

    return (
        <div className={styles.container}>
            <h2>{t("instanceVersionSectionTitle")}</h2>
            {instanceStatus.updateAvailable ? (
                <div className={styles.notice}>
                    <span className={styles.noticeTitle}>
                        {t("instanceVersionUpdateAvailable")}: {instanceStatus.latestVersion}
                    </span>
                    <span>{t("instanceVersionUpdateCommand")}</span>
                    <code className={styles.command}>{UPDATE_COMMAND}</code>
                </div>
            ) : (
                <span className={styles.muted}>
                    {instanceStatus.latestVersion ? t("instanceVersionUpToDate") : t("instanceVersionUnknown")}
                </span>
            )}
            <div className={styles.rows}>
                <InfoRow label={t("instanceVersionCurrent")} value={instanceStatus.version}/>
                <InfoRow label={t("instanceVersionUpdateCheck")} value={statusText(instanceStatus.updateCheckEnabled)}/>
                <InfoRow label={t("instanceVersionUsageReport")} value={statusText(instanceStatus.usageReportEnabled)}/>
                {instanceStatus.lastCheckDate &&
                    <InfoRow label={t("instanceVersionLastCheck")}
                             value={new Date(instanceStatus.lastCheckDate).toLocaleString()}/>}
            </div>
            <span className={styles.muted}>
                {t("instanceVersionTelemetryInfo")}{" "}
                <a href={TELEMETRY_DOCS_URL} target="_blank" rel="noopener noreferrer">
                    {t("instanceVersionDocsLink")}
                </a>
            </span>
        </div>
    );
};

export default InstanceVersionSection;
