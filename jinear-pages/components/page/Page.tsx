import cn from "classnames";
import React from "react";
import styles from "./Page.module.css";

interface PageProps {
  marginVertically?: boolean;
  children?: React.ReactNode;
}

const Page: React.FC<PageProps> = ({ marginVertically = false, children }) => {
  return <div className={cn(styles.container, marginVertically && styles.marginVertically)}>{children}</div>;
};

export default Page;
