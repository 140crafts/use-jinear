import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {WorkspaceDto} from "@/be/jinear-core.ts";
import {useRetrieveUnreadNotificationCountQuery} from "@/api/notificationEventApi.ts";
import useTranslation from "@/locals/useTranslation.ts";
import React from "react";
import {LuBell} from "react-icons/lu";
import styles from "./InboxButton.module.css";

interface InboxButtonProps {
    workspace?: WorkspaceDto | null;
    isActive: boolean;
}

const InboxButton: React.FC<InboxButtonProps> = ({isActive, workspace}) => {
    const {t} = useTranslation();

    const {data: countResponse} = useRetrieveUnreadNotificationCountQuery(
        {workspaceId: workspace?.workspaceId || ""},
        {skip: workspace == null}
    );
    const unreadCount = countResponse?.unreadNotificationCount ? countResponse?.unreadNotificationCount : 0;
    const unreadLabel = unreadCount == 0 ? "" : unreadCount > 99 ? "99+" : `${unreadCount}`;

    return (
        <Button
            href={`/${workspace?.username}/inbox`}
            data-tooltip={t("mainFeaturesMenuLabelNotifications")}
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.short}

        >
            <div className={styles.iconContainer}>
                <LuBell className={'icon'}/>
                {unreadCount != 0 && <div className={styles.unreadWrapper}>{unreadLabel}</div>}
            </div>
        </Button>
    );
};

export default InboxButton;
