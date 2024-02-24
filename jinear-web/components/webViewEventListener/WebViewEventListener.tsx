"use client";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import { submitAnyModalVisibleChangeWebviewEvent } from "@/utils/webviewUtils";
import React, { useEffect } from "react";

interface WebViewEventListenerProps {}

const WebViewEventListener: React.FC<WebViewEventListenerProps> = ({}) => {
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);

  useEffect(() => {
    submitAnyModalVisibleChangeWebviewEvent(isAnyModalVisible);
  }, [isAnyModalVisible]);

  return null;
};

export default WebViewEventListener;
