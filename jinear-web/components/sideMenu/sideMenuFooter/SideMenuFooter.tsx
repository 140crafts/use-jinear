"use client";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
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
  const currentAccount = useTypedSelector(selectCurrentAccount);

  const popAccProfileModal = () => {
    dispatch(popAccountProfileModal());
  };

  return (
    <div className={cn(styles.container, className)}>
      <Button
        variant={ButtonVariants.hoverFilled}
        className={styles.accountButton}
        heightVariant={ButtonHeight.short}
        onClick={popAccProfileModal}
      >
        <span className={styles.userName}>
          {shortenStringIfMoreThanMaxLength({ text: currentAccount?.username || "", maxLength: 18 })}
        </span>
        <div>
          {currentAccount ? (
            <ProfilePhoto
              boringAvatarKey={currentAccount.accountId}
              url={currentAccount.profilePicture?.url}
              wrapperClassName={styles.profilePic}
            />
          ) : (
            <IoPerson size={14} />
          )}
        </div>
      </Button>
      <ThemeToggle variant={ButtonVariants.hoverFilled} />
    </div>
  );
};

export default SideMenuFooter;
