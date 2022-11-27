import cn from "classnames";
import { motion } from "framer-motion";
import React from "react";
import { useActiveTab } from "../context/TabContext";
import styles from "./TabView.module.css";

export interface TabViewProps {
  name: string;
  label: string;
  children: React.ReactNode;
}

const TabView: React.FC<TabViewProps> = ({ name, label, children }) => {
  const activeTab = useActiveTab();
  const isActive = name == activeTab?.name;

  const animations = {
    layout: true,
    initial: "out",
    animate: isActive ? "in" : "out",
    variants: {
      in: {
        scale: 1,
        opacity: 1,
        zIndex: "unset",
      },
      out: {
        scale: 1,
        opacity: 0,
        zIndex: -1,
        transition: { duration: 0 },
      },
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isActive,
  };
  return !isActive ? null : (
    <motion.div
      {...animations}
      className={cn(styles.container, isActive && styles.active)}
    >
      {children}
    </motion.div>
  );
};

export default TabView;
