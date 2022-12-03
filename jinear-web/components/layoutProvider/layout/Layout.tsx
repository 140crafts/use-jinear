import Button from "@/components/button";
import SideMenu from "@/components/sideMenu/SideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import TabBar from "@/components/tabBar/TabBar";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import {
  closeMenu,
  selectAppMenuVisible,
} from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import React from "react";
import { IoChevronDownSharp } from "react-icons/io5";
import styles from "./Layout.module.scss";

interface LayoutProps {
  navbar?: boolean;
  tabbar?: boolean;
  children?: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);
  const dispatch = useAppDispatch();
  //only in desktop
  const isMenuVisible = useTypedSelector(selectAppMenuVisible);

  const _closeMenu = () => {
    dispatch(closeMenu());
  };

  return (
    <div className={styles.layoutMain}>
      {isLoggedIn && (
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

      {isLoggedIn && (
        <div className={styles.tabBarContainer}>
          <TabBar />
        </div>
      )}
    </div>
  );
};

export default Layout;
