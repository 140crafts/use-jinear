import Button, { ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { useLogoutMutation } from "@/store/api/authApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoLogOutOutline, IoPerson } from "react-icons/io5";
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
      <Button href={"/profile"} variant={ButtonVariants.hoverFilled} className={styles.accountButton}>
        <div>
          {currentAccount ? (
            <ProfilePhoto
              boringAvatarKey={currentAccount.accountId}
              storagePath={currentAccount.profilePicture?.storagePath}
              wrapperClassName={styles.profilePic}
            />
          ) : (
            <IoPerson size={14} />
          )}
        </div>
        {shortenStringIfMoreThanMaxLength({ text: currentAccount?.username || "", maxLength: 18 })}
      </Button>{" "}
      <ThemeToggle variant={ButtonVariants.hoverFilled} />
      <Button
        loading={isLoading}
        variant={ButtonVariants.hoverFilled}
        data-tooltip-right={t("sideMenuFooterLogout")}
        onClick={popAreYouSureModalForLogout}
      >
        <IoLogOutOutline size={17} />
      </Button>
    </div>
  );
};

export default SideMenuFooter;
