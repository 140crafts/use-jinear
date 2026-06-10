import { motion } from "framer-motion";
import React from "react";
import styles from "./SelectedNavBottomBar.module.css";

interface SelectedNavBottomBarProps {}

const SelectedNavBottomBar: React.FC<SelectedNavBottomBarProps> = ({}) => {
  return (
    <motion.span
      layoutId="selected-nav-bottom-bar"
      className={styles.container}
      initial={false}
      transition={{
        type: "spring",
        stiffness: 500,
        damping: 50,
        mass: 2,
      }}
    />
  );
};

export default SelectedNavBottomBar;
