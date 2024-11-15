"use client";
import useWidthLimit from "@/hooks/useWidthLimit";
import { selectAnyMenuVisible } from "@/store/slice/displayPreferenceSlice";
import { selectAnyModalVisible } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React, { useEffect } from "react";
import isPwa from "@/utils/pwaHelper";

interface BodyFixerProps {
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const logger = Logger("BodyFixer");
const BodyFixer: React.FC<BodyFixerProps> = ({}) => {
  const isAnyModalVisible = useTypedSelector(selectAnyModalVisible);
  const isAnyMenuVisible = useTypedSelector(selectAnyMenuVisible);
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const pwa = isPwa();

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

  useEffect(()=>{
    if (document && window && pwa){
      const $html = document.querySelector("html");
      $html?.classList.add('noselect');
    }
  },[pwa])

  return null;
};

export default BodyFixer;
