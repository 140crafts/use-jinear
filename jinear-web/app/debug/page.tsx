"use client";
import Button from "@/components/button";
import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      {/* <DebugScreenComp /> */}
      <Button href={"/asd"} className={styles.button0}>
        123
      </Button>
      <Button href={"/asd2"} className={styles.button}>
        1234
      </Button>
    </div>
  );
};

export default DebugScreen;
