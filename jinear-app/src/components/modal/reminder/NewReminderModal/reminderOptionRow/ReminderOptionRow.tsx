import cn from "classnames";
import React, {type ChangeEvent} from "react";
import styles from "./ReminderOptionRow.module.css";

interface ReminderOptionRowProps {
    id: string;
    label: string;
    detail?: string | null;
    checked: boolean;
    disabled?: boolean;
    onChange: (checked: boolean) => void;
    children?: React.ReactNode;
}

const ReminderOptionRow: React.FC<ReminderOptionRowProps> = ({
                                                                 id,
                                                                 label,
                                                                 detail,
                                                                 checked,
                                                                 disabled = false,
                                                                 onChange,
                                                                 children
                                                             }) => {
    const changeHandler = (event: ChangeEvent<HTMLInputElement>) => {
        onChange(event.target.checked);
    };

    return (
        <div className={cn(styles.container, disabled && styles.disabled)}>
            <label className={styles.header} htmlFor={id}>
                <input id={id} type="checkbox" checked={checked && !disabled} disabled={disabled}
                       onChange={changeHandler}/>
                <div className={styles.text}>
                    <b>{label}</b>
                    {detail && <span className={styles.detail}>{detail}</span>}
                </div>
            </label>
            {checked && !disabled && children && <div className={styles.content}>{children}</div>}
        </div>
    );
};

export default ReminderOptionRow;
