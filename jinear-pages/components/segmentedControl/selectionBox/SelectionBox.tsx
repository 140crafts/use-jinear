import { motion } from "framer-motion";
import React from "react";
import styles from "./SelectionBox.module.css";

interface SelectionBoxProps {
  id: string;
}

const SelectionBox: React.FC<SelectionBoxProps> = ({ id }) => {
  return (
    <motion.span
      layoutId={`${id}-segment-selection-box`}
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

export default SelectionBox;
