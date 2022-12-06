import Button from "@/components/button";
import SideMenu from "@/components/sideMenu/SideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import TabBar from "@/components/tabBar/TabBar";
import { selectAuthState } from "@/store/slice/accountSlice";
import {
  closeMenu,
  selectAppMenuVisible,
} from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
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
  const authState = useTypedSelector(selectAuthState);
  const dispatch = useAppDispatch();
  //only in desktop
  const isMenuVisible = useTypedSelector(selectAppMenuVisible);

  useEffect(() => {
    if (authState == "NOT_LOGGED_IN") {
      router.replace("/");
    }
  }, [authState]);

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
          <SideMenu />
          <SideMenuFooter className={styles.sideMenuFooter} />
        </div>
      )}
      <div className={styles.content}>{children}</div>

      {authState == "LOGGED_IN" && (
        <div className={styles.tabBarContainer}>
          <TabBar />
        </div>
      )}
    </div>
  );
};

export default Layout;
