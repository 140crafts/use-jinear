import React from "react";
import styles from "./SelectionBox.module.css";

interface SelectionBoxProps {
  id: string;
}

const SelectionBox: React.FC<SelectionBoxProps> = ({ id }) => {
  return (
    <span
      id={`${id}-segment-selection-box`}
      className={styles.container}
    />
  );
};

export default SelectionBox;
