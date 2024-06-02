"use client";
import React, { useEffect } from "react";
import styles from "./index.module.css";
import { useAppDispatch } from "@/store/store";
import { changeLoadingModalVisibility } from "@/slice/modalSlice";
import { getUnreadConversationCount } from "../../repository/IndexedDbRepository";

interface DebugScreenProps {
}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  const dispatch = useAppDispatch();
  getUnreadConversationCount("01htna4vf2d6waswmy7d5wvanc");
  useEffect(() => {
    setTimeout(() => {
      dispatch(changeLoadingModalVisibility({ visible: true }));
    }, 2500);
  }, [dispatch]);

  return <div className={styles.container}></div>;
};

export default DebugScreen;
