import Button from "@/components/button";
import SideMenu from "@/components/sideMenu/SideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import TabBar from "@/components/tabBar/TabBar";
import WorkspaceMenu from "@/components/workspaceMenu/WorkspaceMenu";
import { selectAuthState } from "@/store/slice/accountSlice";
import { closeMenu, selectAppMenuVisible } from "@/store/slice/displayPreferenceSlice";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import isPwa from "@/utils/pwaHelper";
import cn from "classnames";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { IoChevronDownSharp } from "react-icons/io5";
import styles from "./Layout.module.scss";

interface LayoutProps {
  navbar?: boolean;
  tabbar?: boolean;
  children?: React.ReactNode;
}

const logger = Logger("Layout");

const ROUTES_WITHOUT_SIDE_MENU = [
  "/forgot-password",
  "/register",
  "/login",
  "/engage/[token]/confirm-email",
  "/engage/[token]/reset-password-complete",
  "/engage/[token]/forgot-password",
  "/engage/[token]/workspace-invitation",
];

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const router = useRouter();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const authState = useTypedSelector(selectAuthState);
  const dispatch = useAppDispatch();
  //only in desktop
  const isMenuVisible = useTypedSelector(selectAppMenuVisible);
  const pwa = isPwa();

  logger.log({ pathName: router.pathname, isAnyModalVisible, authState, isMenuVisible, pwa });

  useEffect(() => {
    _closeMenu();
  }, [router.asPath]);

  useEffect(() => {
    if (document && window) {
      if (isAnyModalVisible || isMenuVisible) {
        document.body.style.top = `-${window.scrollY}px`;
        document.body.style.width = `100%`;
        document.body.style.position = "fixed";
      } else {
        const scrollY = document.body.style.top;
        document.body.style.position = "";
        document.body.style.top = "";
        window.scrollTo(0, parseInt(scrollY || "0") * -1);
      }
    }
  }, [isAnyModalVisible, isMenuVisible]);

  const sideMenuEnabled = authState == "LOGGED_IN" && ROUTES_WITHOUT_SIDE_MENU.indexOf(router.pathname) == -1;

  const _closeMenu = () => {
    dispatch(closeMenu());
  };

  return (
    <div className={styles.layoutMain}>
      {sideMenuEnabled && (
        <div
          id="side-menu"
          className={styles.sideMenuContainer}
          style={{
            marginBottom: isMenuVisible ? 0 : "-100vh",
          }}
        >
          <Button className={styles.closeMenuContainer} onClick={_closeMenu}>
            <IoChevronDownSharp size={17} />
          </Button>
          <div className={styles.menuContent}>
            <div className={styles.workspaceMenu}>
              <WorkspaceMenu />
            </div>
            <div className={styles.mainMenu}>
              <SideMenu />
            </div>
          </div>
          <SideMenuFooter
            className={cn(styles.sideMenuFooter, !isMenuVisible && styles.sideMenuFooterMenuOpen, pwa && styles.pwaPadding)}
          />
        </div>
      )}
      <div className={cn(styles.content, !sideMenuEnabled && styles.contentWithoutSideMenu)}>{children}</div>

      {sideMenuEnabled && (
        <div className={cn(styles.tabBarContainer, pwa && styles.pwaPadding)}>
          <TabBar />
        </div>
      )}
    </div>
  );
};

export default Layout;
