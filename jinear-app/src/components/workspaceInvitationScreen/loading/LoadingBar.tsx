import React from "react";
import styles from "./LoadingBar.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface LoadingBarProps {
    isFetching: boolean;
}

const LoadingBar: React.FC<LoadingBarProps> = ({isFetching}) => {
    return isFetching ? (
        <div className={styles.loadingContainer}>
            <CircularLoading size={18}/>
        </div>
    ) : null;
};

export default LoadingBar;
