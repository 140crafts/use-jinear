"use client";
import Button from "@/components/button";
import { useQueryState, useSetQueryState } from "@/hooks/useQueryState";
import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const sucuk = useQueryState<string>("sucuk");
  const setQueryState = useSetQueryState();
  return (
    <div className={styles.container}>
      <h1>{sucuk}</h1>
      <Button
        onClick={() => {
          setQueryState("sucuk", `${parseInt(`${Math.random() * 100}`)}`);
        }}
        className={styles.button0}
      >
        Set Random
      </Button>
      <Button
        onClick={() => {
          setQueryState("sucuk", undefined);
        }}
        className={styles.button0}
      >
        Clear
      </Button>
    </div>
  );
};

export default DebugScreen;
