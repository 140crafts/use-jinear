import React, { useMemo } from "react";
import styles from "./MediaLimitUsage.module.css";
import { useRetrieveWorkspaceMediaLimitsQuery } from "@/api/workspaceMediaApi";
import { humanReadibleFileSize } from "@/util/FileSizeFormatter";
import useTranslation from "@/locals/useTranslation";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import type { WorkspaceDto } from "@/be/jinear-core";

interface MediaLimitUsageProps {
  workspace: WorkspaceDto;
}

const MediaLimitUsage: React.FC<MediaLimitUsageProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const workspaceId = workspace.workspaceId;
  const { data: usageLimitResponse } = useRetrieveWorkspaceMediaLimitsQuery({ workspaceId });
  const usageData = useMemo(() => {
    if (!usageLimitResponse) {
      return null;
    }

    const currentTotal = usageLimitResponse.data.currentTotal ?? 0;
    const storageLimit = usageLimitResponse.data.storageLimit ?? 0;

    let percentage = 0;
    if (storageLimit > 0 && !isNaN(currentTotal) && !isNaN(storageLimit)) {
      const value = (100 * currentTotal) / storageLimit;
      if (!isNaN(value) && isFinite(value)) {
        percentage = parseFloat(value.toFixed(2));
      }
    }

    return {
      percentage,
      currentTotal: humanReadibleFileSize(currentTotal * 1024 * 1024),
      storageLimit: humanReadibleFileSize(storageLimit * 1024 * 1024)
    };
  }, [usageLimitResponse]);

  return (
    <div>
      {usageLimitResponse && usageData &&
        <div className={styles.container}>
          <div className={styles.progressContainer}>
            <div
              style={{ flex: usageLimitResponse.data.currentTotal }}
              className={styles.progressFilled} />
            <div
              style={{ flex: usageLimitResponse.data.storageLimit - usageLimitResponse.data.currentTotal }}
            />
          </div>
          <span>
            {t("workspaceLimitQuotaText").replace("{usage}", usageData.currentTotal).replace("{percentage}", `${usageData.percentage}`).replace("{limit}", usageData.storageLimit)}
          </span>
          <WorkspaceUpgradeButton
            workspace={workspace}
            variant={"FULL"}
            className={styles.upgradeButton} />
        </div>
      }
    </div>
  );
};

export default MediaLimitUsage;