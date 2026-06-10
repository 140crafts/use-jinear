import React, {type ChangeEvent} from "react";
import styles from "./SettingsCheckbox.module.css";

interface SettingsCheckboxProps {
    id: string;
    label: string;
    text: string;
    checked: boolean;
    onChange: (checked: boolean) => void;
}

const SettingsCheckbox: React.FC<SettingsCheckboxProps> = ({id, label, text, checked, onChange}) => {
    const changeHandler = (event: ChangeEvent<HTMLInputElement>) => {
        onChange(event.target.checked);
    };

    return (
        <label className={styles.label} htmlFor={id}>
            <input id={id} type="checkbox" checked={checked} onChange={changeHandler}/>
            <div className={styles.textContainer}>
                <h3>{label}</h3>
                <span className={styles.info}>{text}</span>
            </div>
        </label>
    );
};

export default SettingsCheckbox;