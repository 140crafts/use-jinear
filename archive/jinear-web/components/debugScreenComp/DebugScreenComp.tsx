"use client";
import React from "react";
import Button from "../button";
import styles from "./DebugScreenComp.module.css";

interface DebugScreenProps {}

const DebugScreenComp: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <Button href={"/asd"} className={styles.zort}>
        123
      </Button>
      <Button href={"/asd2"} className={styles.zort2}>
        1234
      </Button>
    </div>
  );
};

export default DebugScreenComp;
