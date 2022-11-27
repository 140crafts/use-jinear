import Button from "@/components/button";
import { motion, usePresence } from "framer-motion";
import React from "react";
import { useActiveTab, useChangeTab } from "../../context/TabContext";
import styles from "./NavigatorButton.module.css";
import SelectedNavBottomBar from "./selectedNavBottomBar/SelectedNavBottomBar";

interface NavigatorButtonProps {
  name: string;
  label: string;
}

const NavigatorButton: React.FC<NavigatorButtonProps> = ({ name, label }) => {
  const activeTab = useActiveTab();
  const changeTab = useChangeTab();
  const isActive = activeTab?.name == name;
  const [isPresent, safeToRemove] = usePresence();

  const animations = {
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        scale: 1,
        opacity: 1,
      },
      out: {
        scale: 0.1,
        opacity: 0,
      },
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent && safeToRemove?.(),
  };

  const onClick = () => {
    changeTab?.(name);
  };

  return (
    <motion.div {...animations} className={styles.container}>
      {isActive && <SelectedNavBottomBar />}
      <Button onClick={onClick}>{label}</Button>
    </motion.div>
  );
};

export default NavigatorButton;
