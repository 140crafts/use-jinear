import React from "react";
import styles from "./Breadcrumb.module.css";

interface BreadcrumbProps {
  children: React.ReactNode;
}

const Breadcrumb: React.FC<BreadcrumbProps> = ({ children }) => {
  return (
    <div className={styles.container}>
      {React.Children.map(children, (child, i) => (
        <>
          {i != 0 ? <div className={styles.slash}>/</div> : null}
          {child}
        </>
      ))}
    </div>
  );
};

export default Breadcrumb;
