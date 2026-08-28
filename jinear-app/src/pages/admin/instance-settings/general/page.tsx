import InstanceFlagsSection from "@/components/instanceSettingsScreen/instanceFlagsSection/InstanceFlagsSection";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./page.module.css";

interface InstanceGeneralSettingsPageProps {
}

const InstanceGeneralSettingsPage: React.FC<InstanceGeneralSettingsPageProps> = ({}) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <InstanceFlagsSection title={t("instanceSettingsGeneralTitle")}/>
            <div className="spacer-h-4"/>
        </div>
    );
};

export default InstanceGeneralSettingsPage;
