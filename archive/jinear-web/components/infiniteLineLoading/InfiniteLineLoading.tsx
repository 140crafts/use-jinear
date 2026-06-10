import React from "react";
import styles from "./InfiniteLineLoading.module.css";
import cn from "classnames";

interface InfiniteLineLoadingProps {
  className?: string;
}

const InfiniteLineLoading: React.FC<InfiniteLineLoadingProps> = ({ className }) => {

  return (
    <div className={cn(styles["infinite-loader"], className)}>
    </div>
  );
};

export default InfiniteLineLoading;