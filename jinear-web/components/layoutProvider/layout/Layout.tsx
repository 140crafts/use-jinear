import Button from "@/components/button";
import SideMenu from "@/components/sideMenu/SideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import TabBar from "@/components/tabBar/TabBar";
import WorkspaceMenu from "@/components/workspaceMenu/WorkspaceMenu";
import { selectAuthState } from "@/store/slice/accountSlice";
import {
  closeMenu,
  selectAppMenuVisible,
} from "@/store/slice/displayPreferenceSlice";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
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

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const router = useRouter();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const authState = useTypedSelector(selectAuthState);
  const dispatch = useAppDispatch();
  //only in desktop
  const isMenuVisible = useTypedSelector(selectAppMenuVisible);
  const pwa = isPwa();

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

  const _closeMenu = () => {
    dispatch(closeMenu());
  };

  return (
    <div className={styles.layoutMain}>
      {authState == "LOGGED_IN" && (
        <div
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
            className={cn(
              styles.sideMenuFooter,
              !isMenuVisible && styles.sideMenuFooterMenuOpen,
              pwa && styles.pwaPadding
            )}
          />
        </div>
      )}
      <div className={styles.content}>{children}</div>

      {authState == "LOGGED_IN" && (
        <div className={cn(styles.tabBarContainer, pwa && styles.pwaPadding)}>
          <TabBar />
        </div>
      )}
    </div>
  );
};

export default Layout;
