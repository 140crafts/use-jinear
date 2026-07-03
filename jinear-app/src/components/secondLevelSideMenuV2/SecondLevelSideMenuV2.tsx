import React from "react";
import styles from "./SecondLevelSideMenuV2.module.scss";
import cn from "classnames";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import Modal from "@/components/modal/modal/Modal";
import {useAppDispatch, useTypedSelector} from "@/store";
import {closeSecondLevelMenu, popSecondLevelMenu, selectSecondLevelMenuVisible} from "@/slice/displayPreferenceSlice";
import useWindowSize from "@/hooks/useWindowSize.ts";

interface SecondLevelSideMenuV2Props {
    className?: string;
    children: React.ReactNode;
    mobileFabButtonText: string;
    mobileFabButtonIcon: React.ReactNode;
}

const SecondLevelSideMenuV2: React.FC<SecondLevelSideMenuV2Props> = ({
                                                                         className,
                                                                         mobileFabButtonText,
                                                                         mobileFabButtonIcon,
                                                                         children
                                                                     }) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const menuModalVisible = useTypedSelector(selectSecondLevelMenuVisible);
    const {isTablet} = useWindowSize();

    const popMenu = () => {
        dispatch(popSecondLevelMenu());
    };

    const closeMenu = () => {
        dispatch(closeSecondLevelMenu());
    };

    return (
        <div className={cn(styles.sideMenu, className)}>
            <div id="second-level-side-menu-content"
                 className={cn(styles.sideMenuContent)}>
                {children}
            </div>
            <div id="second-level-side-menu-mobile-menu-button-container" className={styles.mobileMenuButtonContainer}>
                <Button heightVariant={ButtonHeight.mid} variant={ButtonVariants.brandColor}
                        className={styles.mobileMenuButton} onClick={popMenu}>
                    {mobileFabButtonIcon}
                    <b>{mobileFabButtonText ?? t("tasksLayoutSideMenuCollapsedLabel")}</b>
                </Button>
            </div>
            <div>
                <Modal
                    visible={isTablet && menuModalVisible}
                    bottomSheet
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