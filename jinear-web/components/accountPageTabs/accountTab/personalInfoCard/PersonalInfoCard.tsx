import Button, { ButtonVariants } from "@/components/button";
import { useLogoutMutation } from "@/store/api/authApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./PersonalInfoCard.module.css";

interface PersonalInfoCardProps {}

const PersonalInfoCard: React.FC<PersonalInfoCardProps> = ({}) => {
  const { t } = useTranslation();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const [logout, { isLoading }] = useLogoutMutation();

  return (
    <div className={styles.container}>
      <h2>{t("accountTabPersonalInfoTitle")}</h2>
      <label className={styles.label}>
        {t("accountTabUsername")}
        <input
          disabled
          className={styles.input}
          value={currentAccount?.username || ""}
        />
      </label>

      <label className={styles.label}>
        {t("accountTabEmail")}
        <input
          disabled
          className={styles.input}
          value={currentAccount?.email}
        />
      </label>

      <Button
        disabled={isLoading}
        loading={isLoading}
        onClick={logout}
        variant={ButtonVariants.contrast}
        className={styles.button}
      >
        {t("logoutButtonLabel")}
      </Button>
    </div>
  );
};

export default PersonalInfoCard;
