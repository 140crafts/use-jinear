"use client";
import { closeDeviceOfflineModal, popDeviceOfflineModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import React, { useEffect } from "react";

interface OfflineListenerProps {}

const logger = Logger("OfflineListener");

const OfflineListener: React.FC<OfflineListenerProps> = ({}) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (typeof window === "object") {
      window.addEventListener("offline", (e) => {
        logger.log("Device Offline");
        dispatch(popDeviceOfflineModal());
      });

      window.addEventListener("online", (e) => {
        logger.log("Device Online");
        dispatch(closeDeviceOfflineModal());
      });
    }
  }, []);
  return null;
};

export default OfflineListener;
