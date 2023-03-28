import Button, { ButtonVariants } from "@/components/button";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { useLogoutRequestMutation } from "@/store/api/accountApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoLogOutOutline } from "react-icons/io5";
import styles from "./SideMenuFooter.module.css";

interface SideMenuFooterProps {
  className?: string;
}

const SideMenuFooter: React.FC<SideMenuFooterProps> = ({ className }) => {
  const { t } = useTranslation();
  const [logout, { isLoading, isError }] = useLogoutRequestMutation();
  const currentAccount = useTypedSelector(selectCurrentAccount);

  return (
    <div className={cn(styles.container, className)}>
      <ThemeToggle />
      <Button variant={ButtonVariants.hoverFilled} className={styles.accountButton}>
        {currentAccount?.username}
      </Button>
      <Button
        loading={isLoading}
        variant={ButtonVariants.hoverFilled}
        data-tooltip-top-right={t("sideMenuFooterLogout")}
        onClick={logout}
      >
        <IoLogOutOutline size={19} />
      </Button>
    </div>
  );
};

export default SideMenuFooter;
