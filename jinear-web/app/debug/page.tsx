"use client";
import Button from "@/components/button";
import { submitNotificationStateRequestWebviewEvent } from "@/utils/webviewUtils";
import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <Button
        onClick={() => {
          submitNotificationStateRequestWebviewEvent();
        }}
        className={styles.button0}
      >
        submitNotificationStateRequestWebviewEvent
      </Button>
    </div>
  );
};

export default DebugScreen;
