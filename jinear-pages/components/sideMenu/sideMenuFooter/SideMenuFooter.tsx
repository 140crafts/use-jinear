"use client";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectAuthState, selectCurrentAccount } from "@/store/slice/accountSlice";
import { popAccountProfileModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPerson } from "react-icons/io5";
import styles from "./SideMenuFooter.module.scss";

interface SideMenuFooterProps {
  className?: string;
}

const SideMenuFooter: React.FC<SideMenuFooterProps> = ({ className }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const authState = useTypedSelector(selectAuthState);
  const currentAccount = useTypedSelector(selectCurrentAccount);

  const popAccProfileModal = () => {
    currentAccount && dispatch(popAccountProfileModal());
  };

  return (
    <div className={cn(styles.container, className)}>
      {authState != "NOT_DECIDED" &&
        <Button
          variant={ButtonVariants.hoverFilled2}
          className={styles.accountButton}
          heightVariant={ButtonHeight.short}
          onClick={popAccProfileModal}
          href={currentAccount ? undefined : "/login"}
        >
        <span className={styles.userName}>
          {currentAccount ? shortenStringIfMoreThanMaxLength({
            text: currentAccount?.username || "",
            maxLength: 18
          }) : t("loginScreenTitle")}
        </span>
          {currentAccount &&
            <ProfilePhoto
              boringAvatarKey={currentAccount.accountId}
              url={currentAccount.profilePicture?.url}
              wrapperClassName={styles.profilePic}
            />}
        </Button>
      }
    </div>
  );
};

export default SideMenuFooter;
