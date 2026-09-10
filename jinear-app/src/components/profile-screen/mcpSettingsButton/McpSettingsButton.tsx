import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locales/useTranslation";
import {useRetrieveMcpServerInfoQuery} from "@/store/api/mcpApi";
import {popMcpSettingsModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import React from "react";
import styles from "./McpSettingsButton.module.css";

interface McpSettingsButtonProps {
}

const McpSettingsButton: React.FC<McpSettingsButtonProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const {currentData: serverInfoResponse} = useRetrieveMcpServerInfoQuery();

    if (serverInfoResponse?.data?.enabled != true || !serverInfoResponse.data.serverUrl) {
        return null;
    }

    const openMcpSettings = () => {
        dispatch(popMcpSettingsModal());
    };

    return (
        <div className={styles.container}>
            <h2>{t("mcpConnectionsTitle")}</h2>
            <span className={styles.text}>{t("mcpSettingsSectionText")}</span>
            <div className={styles.contentContainer}>
                <Button variant={ButtonVariants.filled} onClick={openMcpSettings}>
                    {t("mcpSettingsButtonLabel")}
                </Button>
            </div>
        </div>
    );
};

export default McpSettingsButton;
