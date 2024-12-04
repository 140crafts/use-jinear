"use client";
import React from "react";
import styles from "./index.module.css";
import { useAppDispatch } from "@/store/store";
import { popProjectAndMilestonePickerModal } from "@/slice/modalSlice";
import Button from "@/components/button";

interface DebugScreenProps {
}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const dispatch = useAppDispatch();

  const pop = () => {
    dispatch(popProjectAndMilestonePickerModal({ workspaceId: "01j7tgp8s9v74xvtm60hcabwt2", visible: true }));
  };

  return <div className={styles.container}>
    <Button onClick={pop}>pop</Button>
  </div>;
};

export default DebugScreen;
