import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./OverlayLoading.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface OverlayLoadingProps {
    isFetching: boolean;
}

const OverlayLoading: React.FC<OverlayLoadingProps> = ({isFetching}) => {
    const {t} = useTranslation();
    return isFetching ? (
        <div className={styles.loadingContainer}>
            <CircularLoading size={14}/>
            <span>{t("calendarSyncing")}</span>
        </div>
    ) : null;
};

export default OverlayLoading;
