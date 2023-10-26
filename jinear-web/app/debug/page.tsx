"use client";
import Button from "@/components/button";
import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <Button href={"/asd"} className={styles.zort}>
        123
      </Button>
    </div>
  );
};

export default DebugScreen;
