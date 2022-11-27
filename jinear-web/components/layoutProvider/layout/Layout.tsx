import React from "react";
import styles from "./Layout.module.css";

interface LayoutProps {
  navbar?: boolean;
  tabbar?: boolean;
  children?: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({
  children,
  navbar = true,
  tabbar = true,
}) => {
  return (
    <>
      {/* {navbar ? <Navbar /> : null} */}
      <div className={styles.layoutMain}>{children}</div>
      {/* {tabbar && <FloatingTabBar />} */}
    </>
  );
};

export default Layout;
