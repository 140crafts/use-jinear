"use client";
import { clearReroute, selectReroute } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";

interface InternalWorkspacePrefChangeListenerProps {}

const logger = Logger("InternalWorkspacePrefChangeListener");

const InternalWorkspacePrefChangeListener: React.FC<InternalWorkspacePrefChangeListenerProps> = ({}) => {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const activeReroute = useTypedSelector(selectReroute);

  const clearActiveReroute = () => {
    dispatch(clearReroute());
  };

  useEffect(() => {
    logger.log({ activeReroute });
    if (activeReroute) {
      router.replace(activeReroute);
      clearActiveReroute();
    }
  }, [activeReroute]);

  return null;
};

export default InternalWorkspacePrefChangeListener;
