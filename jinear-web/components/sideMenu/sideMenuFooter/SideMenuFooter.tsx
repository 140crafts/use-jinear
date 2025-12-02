"use client";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { popAccountProfileModal, popFeedbackModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoBulbOutline, IoPerson } from "react-icons/io5";
import styles from "./SideMenuFooter.module.scss";
import { env } from "next-runtime-env";
import { __DEV__ } from "@/utils/constants";

interface SideMenuFooterProps {
  className?: string;
}

const suggestToken = !__DEV__ ? env("NEXT_PUBLIC_SUGGEST_TOKEN") : "01k8jrynssn1nawjg5xy2ejyjc";
const suggestThreadId = !__DEV__ ? env("NEXT_PUBLIC_SUGGEST_THREAD_ID") : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMWoxMjV2ZmVmbm0yMTk2c3h4YnJnemFhNiIsImlzX3JvYm90Ijp0cnVlLCJleHAiOjQ4NzI3Mzc5OTAsImlhdCI6MTcxOTEzNzk5MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ST0JPVCJdfQ.BZYk_Cnd5cvDIBS94qY4E_KAJHyUAlZMmJ4eG65hMjPuG3RlC0U-q5PdYYnwsPay5PS7xSWcWPSTYgXIJrjznQ";

const SideMenuFooter: React.FC<SideMenuFooterProps> = ({ className }) => {
  const dispatch = useAppDispatch();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const suggestEnabled = suggestToken && suggestThreadId;

  const popAccProfileModal = () => {
    dispatch(popAccountProfileModal());
  };

  const popSuggestionModal = () => {
    dispatch(popFeedbackModal({ visible: true }));
  };

  return (
    <div className={cn(styles.container, className)}>
      {suggestEnabled &&
        <Button
          variant={ButtonVariants.outline}
          onClick={popSuggestionModal}
          heightVariant={ButtonHeight.short}
          className={styles.iconButton}
        >
          <IoBulbOutline size={14} />
        </Button>}

      <ThemeToggle
        variant={ButtonVariants.hoverFilled}
        buttonStyle={styles.iconButton}
      />

      <Button
        variant={ButtonVariants.hoverFilled}
        className={styles.accountButton}
        heightVariant={ButtonHeight.short}
        onClick={popAccProfileModal}
      >
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
    </div>
  );
};

export default SideMenuFooter;
