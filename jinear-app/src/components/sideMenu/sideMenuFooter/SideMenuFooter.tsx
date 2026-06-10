import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { popAccountProfileModal, popFeedbackModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store";
import cn from "classnames";
import React from "react";
import { IoBulbOutline, IoPerson } from "react-icons/io5";
import styles from "./SideMenuFooter.module.scss";

interface SideMenuFooterProps {
  className?: string;
}

const SideMenuFooter: React.FC<SideMenuFooterProps> = ({ className }) => {
  const dispatch = useAppDispatch();
  const currentAccount = useTypedSelector(selectCurrentAccount);

  const popAccProfileModal = () => {
    dispatch(popAccountProfileModal());
  };

  const popSuggestionModal = () => {
    dispatch(popFeedbackModal({ visible: true }));
  };

  return (
    <div className={cn(styles.container, className)}>
      {/*{suggestEnabled &&*/}
      {/*  <PureClientOnly>*/}
      {/*    <Button*/}
      {/*      variant={ButtonVariants.outline}*/}
      {/*      onClick={popSuggestionModal}*/}
      {/*      heightVariant={ButtonHeight.short}*/}
      {/*      className={styles.iconButton}*/}
      {/*    >*/}
      {/*      <IoBulbOutline size={14} />*/}
      {/*    </Button>*/}
      {/*  </PureClientOnly>}*/}

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
