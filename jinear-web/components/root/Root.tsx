"use client";
import React from "react";
import styles from "./Root.module.css";

interface RootProps {
  children: React.ReactNode;
}

const Root: React.FC<RootProps> = ({ children }) => {
  return <div className={styles.content}>{children}</div>;
};

export default Root;
