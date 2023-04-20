import useTranslation from "locales/useTranslation";
import React from "react";
import InboxScreenBreadcrumbs from "../inboxScreenBreadcrumbs/InboxScreenBreadcrumbs";
import styles from "./InboxScreenHeader.module.css";

interface InboxScreenHeaderProps {}

const InboxScreenHeader: React.FC<InboxScreenHeaderProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <InboxScreenBreadcrumbs />
      {/* <h2>{t("inboxScreenHeader")}</h2> */}
    </div>
  );
};

export default InboxScreenHeader;
