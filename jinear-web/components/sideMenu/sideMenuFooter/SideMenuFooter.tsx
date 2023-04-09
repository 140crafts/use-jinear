import Button, { ButtonVariants } from "@/components/button";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { useLogoutMutation } from "@/store/api/authApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
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
  const dispatch = useAppDispatch();
  const [logoutCall, { isLoading, isError }] = useLogoutMutation();
  const currentAccount = useTypedSelector(selectCurrentAccount);

  const logout = () => {
    logoutCall();
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForLogout = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("logoutAreYouSureTitle"),
        content: t("logoutAreYouSureText"),
        confirmButtonLabel: t("logoutAreYouSureConfirmLabel"),
        onConfirm: logout,
      })
    );
  };

  return (
    <div className={cn(styles.container, className)}>
      <ThemeToggle />
      <Button href={"/profile"} variant={ButtonVariants.hoverFilled} className={styles.accountButton}>
        {currentAccount?.username}
      </Button>
      <Button
        loading={isLoading}
        variant={ButtonVariants.hoverFilled}
        data-tooltip-top-right={t("sideMenuFooterLogout")}
        onClick={popAreYouSureModalForLogout}
      >
        <IoLogOutOutline size={19} />
      </Button>
    </div>
  );
};

export default SideMenuFooter;
