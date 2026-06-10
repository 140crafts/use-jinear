import cn from "classnames";
import React from "react";
import Button from "../button";
import styles from "./FormLogo.module.scss";

interface FormLogoProps {
    withLeftLine?: boolean;
    contentClassName?: string;
    textClassName?: string;
}

const FormLogo: React.FC<FormLogoProps> = ({withLeftLine = true, contentClassName, textClassName}) => {
    return (
        <div className={styles.logoContainer}>
            {withLeftLine && <div className={styles.logoLine}></div>}
            <div className={cn(styles.logoContent, contentClassName)}>
                <div className={styles.logoText} style={{position: "static"}}>
                    <Button href="/" className={cn(styles.logoButton, textClassName)}>
                        JINEAR
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default FormLogo;
