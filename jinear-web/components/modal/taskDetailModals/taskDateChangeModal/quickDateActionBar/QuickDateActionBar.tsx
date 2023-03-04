import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./QuickDateActionBar.module.css";

interface QuickDateActionBarProps {
  setDatesThisWeek: () => void;
  setDatesNextWeek: () => void;
}

const QuickDateActionBar: React.FC<QuickDateActionBarProps> = ({ setDatesThisWeek, setDatesNextWeek }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.quickDatesContainer}>
      <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={setDatesThisWeek}>
        {t("changeTaskDateThisWeek")}
      </Button>
      <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={setDatesNextWeek}>
        {t("changeTaskDateNextWeek")}
      </Button>
    </div>
  );
};

export default QuickDateActionBar;
