import React, { ChangeEvent, useEffect, useState } from "react";
import styles from "./SettingsCheckbox.module.css";

interface SettingsCheckboxProps {
  id: string;
  label: string;
  text: string;
  checked: boolean;
  onChange: (checked: boolean) => void;
}

const SettingsCheckbox: React.FC<SettingsCheckboxProps> = ({ id, label, text, checked, onChange }) => {
  const [_checked, setChecked] = useState<boolean>(checked);

  useEffect(() => {
    setChecked(checked);
  }, [checked]);

  const changeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    const next = event.target.checked;
    setChecked(next);
    onChange?.(next);
  };

  return (
    <label className={styles.label} htmlFor={id}>
      <input id={id} type={"checkbox"} checked={_checked} onChange={changeHandler} />
      <div className={styles.textContainer}>
        <h3>{label}</h3>
        <span>{text}</span>
      </div>
    </label>
  );
};

export default SettingsCheckbox;
