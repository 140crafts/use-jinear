import cn from "classnames";
import React from "react";
import styles from "./CircularLoading.module.css";
import {LuLoaderCircle} from "react-icons/lu";

interface CircularLoadingProps {
    containerClassName?: string;
    progressClassName?: string;
    size?: number;
}

const CircularLoading: React.FC<CircularLoadingProps> = ({containerClassName, progressClassName, size = 14}) => {
    return (
        <div className={cn(styles.container, containerClassName)}>
            <LuLoaderCircle className={cn(styles['animate-spin'], progressClassName)} size={size}/>
        </div>
    );
};

export default CircularLoading;
