import SideMenu from "@/components/sideMenu/SideMenu";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./Layout.module.css";

interface LayoutProps {
  navbar?: boolean;
  tabbar?: boolean;
  children?: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);
  return (
    <div className={styles.layoutMain}>
      {isLoggedIn && (
        <div className={styles.sideMenuContainer}>
          <SideMenu />
        </div>
      )}
      <div className={styles.content}>{children}</div>
    </div>
  );
};

export default Layout;
