"use client";
import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveUnreadNotificationCountQuery } from "@/store/api/notificationEventApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuBell } from "react-icons/lu";
import styles from "./InboxButton.module.css";

interface InboxButtonProps {
  workspace?: WorkspaceDto | null;
  buttonStyle: string;
  iconStyle: string;
  isActive: boolean;
}

const InboxButton: React.FC<InboxButtonProps> = ({ isActive, workspace, buttonStyle, iconStyle }) => {
  const { t } = useTranslation();

  const { data: countResponse } = useRetrieveUnreadNotificationCountQuery(
    { workspaceId: workspace?.workspaceId || "" },
    { skip: workspace == null }
  );
  const unreadCount = countResponse?.unreadNotificationCount ? countResponse?.unreadNotificationCount : 0;
  const unreadLabel = unreadCount == 0 ? "" : unreadCount > 99 ? "99+" : `${unreadCount}`;

  return (
    <Button
      variant={isActive ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
      className={buttonStyle}
      href={`/${workspace?.username}/inbox`}
    >
      <div className={styles.iconContainer}>
        <LuBell className={iconStyle} />
        {unreadCount != 0 && <div className={styles.unreadWrapper}>{unreadLabel}</div>}
      </div>
      {t("mainFeaturesMenuLabelNotifications")}
    </Button>
  );
};

export default InboxButton;
