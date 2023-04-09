import React from "react";
import styles from "./SettingsCheckbox.module.css";

interface SettingsCheckboxProps {
  id: string;
  label: string;
  checked: boolean;
}

const SettingsCheckbox: React.FC<SettingsCheckboxProps> = ({ id, label, checked }) => {
  return (
    <label className={styles.label} htmlFor={id}>
      <input id={id} type={"checkbox"} checked={checked} />
      {label}
    </label>
  );
};

export default SettingsCheckbox;
