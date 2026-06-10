import React from "react";
import styles from "./FormTitle.module.scss";
import {Link} from "react-router-dom";

interface FormTitleProps {
    title: string;
    subTitle?: string;
    linkLabel?: string;
    link?: string;
}

const FormTitle: React.FC<FormTitleProps> = ({title, subTitle, linkLabel, link}) => {
    return (
        <div className={styles.container}>
            <div className={styles.title}>{title}</div>
            <div className={styles.subtitleContainer}>
                <div className={styles.subTitle}>
                    {subTitle}
                    {link && linkLabel &&
                        <Link style={{paddingLeft: 3.5, wordBreak: "break-word"}} to={link}>{` ${linkLabel}`}</Link>}
                </div>
            </div>
        </div>
    );
};

export default FormTitle;
