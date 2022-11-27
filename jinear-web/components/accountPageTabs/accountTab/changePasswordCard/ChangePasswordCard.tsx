import ChangePasswordForm from "@/components/form/changePasswordForm/ChangePasswordForm";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./ChangePasswordCard.module.css";

interface ChangePasswordCardProps {}

const ChangePasswordCard: React.FC<ChangePasswordCardProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <h2>{t("accountTabChangePasswordTitle")}</h2>
      <ChangePasswordForm />
    </div>
  );
};

export default ChangePasswordCard;
