import React from "react";
import styles from "./SecondLevelSideMenuV2.module.scss";
import cn from "classnames";
import Button from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import Modal from "@/components/modal/modal/Modal";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { closeSecondLevelMenu, popSecondLevelMenu, selectSecondLevelMenuVisible } from "@/slice/displayPreferenceSlice";

interface SecondLevelSideMenuV2Props {
  children: React.ReactNode;
}

const SecondLevelSideMenuV2: React.FC<SecondLevelSideMenuV2Props> = ({ children }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const menuModalVisible = useTypedSelector(selectSecondLevelMenuVisible);

  const popMenu = () => {
    dispatch(popSecondLevelMenu());
  };

  const closeMenu = () => {
    dispatch(closeSecondLevelMenu());
  };

  return (
    <div className={styles.sideMenu}>
      <div id="second-level-side-menu-content"
           className={cn(styles.sideMenuContent)}>
        {children}
      </div>
      <div id="second-level-side-menu-mobile-menu-button-container" className={styles.mobileMenuButtonContainer}>
        <Button className={styles.mobileMenuButton} onClick={popMenu}>
          <b>{t("tasksLayoutSideMenuCollapsedLabel")}</b>
        </Button>
      </div>
      <div>
        <Modal
          visible={menuModalVisible}
          bodyClass={styles.modalBody}
          containerClassName={styles.modalContainer}
          contentClassName={styles.modalContent}
          requestClose={closeMenu}
        >
          <Button className={styles.modalMenuButton} onClick={closeMenu}>
            <b>{t("tasksLayoutSideMenuCollapsedLabel")}</b>
          </Button>
          {children}
        </Modal>
      </div>
    </div>
  );
};

export default SecondLevelSideMenuV2;