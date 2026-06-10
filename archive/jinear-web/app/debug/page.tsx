"use client";
import React from "react";
import styles from "./index.module.css";
import { useAppDispatch } from "@/store/store";
import { popProjectAndMilestonePickerModal } from "@/slice/modalSlice";
import Button from "@/components/button";
import { API_ROOT } from "@/utils/constants";
import { env } from "next-runtime-env";

interface DebugScreenProps {
}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const dispatch = useAppDispatch();

  const pop = () => {
    dispatch(popProjectAndMilestonePickerModal({ workspaceId: "01j7tgp8s9v74xvtm60hcabwt2", visible: true }));
  };

  return <div className={styles.container}>
    {`API_ROOT: ${API_ROOT}`}
    <br />
    {`process.env.API_ROOT: ${env("NEXT_PUBLIC_API_ROOT")}`}
    <br />
    {`process.env.NEXT_PUBLIC_ZORT: ${env("NEXT_PUBLIC_ZORT")}`}
  </div>;
};

export default DebugScreen;
