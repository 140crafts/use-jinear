import React from "react";
import styles from "./SelectedNavBottomBar.module.css";

interface SelectedNavBottomBarProps {}

const SelectedNavBottomBar: React.FC<SelectedNavBottomBarProps> = ({}) => {
  return (
    <span
      id="selected-nav-bottom-bar"
      className={styles.container}
    />
  );
};

export default SelectedNavBottomBar;
