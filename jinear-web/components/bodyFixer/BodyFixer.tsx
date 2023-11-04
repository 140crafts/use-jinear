"use client";
import useWidthLimit from "@/hooks/useWidthLimit";
import { closeAllMenus, popAllMenus, selectAnyMenuVisible } from "@/store/slice/displayPreferenceSlice";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

interface BodyFixerProps {}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const logger = Logger("BodyFixer");
const BodyFixer: React.FC<BodyFixerProps> = ({}) => {
  const dispatch = useDispatch();
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const isAnyMenuVisible = useTypedSelector(selectAnyMenuVisible);
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });

  logger.log({ isAnyModalVisible, isAnyMenuVisible, isMobile });

  useEffect(() => {
    if (isMobile) {
      dispatch(closeAllMenus());
    } else {
      dispatch(popAllMenus());
    }
  }, [isMobile]);

  useEffect(() => {
    if (document && window) {
      if (isAnyModalVisible || (isAnyMenuVisible && isMobile)) {
        document.body.style.top = `-${window.scrollY}px`;
        document.body.style.width = `100%`;
        document.body.style.position = "fixed";
      } else {
        const scrollY = document.body.style.top;
        document.body.style.position = "";
        document.body.style.top = "";
        window.scrollTo(0, parseInt(scrollY || "0") * -1);
      }
    }
  }, [isAnyModalVisible, isAnyMenuVisible, isMobile]);

  return null;
};

export default BodyFixer;
