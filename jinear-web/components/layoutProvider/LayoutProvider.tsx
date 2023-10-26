"use client";
import Logger from "@/utils/logger";
import { usePathname, useRouter } from "next/navigation";
import React from "react";
import { PureClientOnly } from "../clientOnly/ClientOnly";
import Layout from "./layout/Layout";

interface LayoutProviderProps {
  children: React.ReactNode;
  navbar?: boolean;
  tabbar?: boolean;
}

const PAGES_WITHOUT_LAYOUT = ["/meeting/[bookingId]", "/meeting/quick/[bookingId]"];

const IGNORE_LAYOUT_IF_PARAM_EXISTS = ["forPrint=true"];

const logger = Logger("LayoutProvider");
const LayoutProvider: React.FC<LayoutProviderProps> = ({ children }) => {
  const router = useRouter();
  const currPath = usePathname() || "";

  const anyHideParamsExists = () => {
    // TODO cgds-275
    // const path = router?.asPath;
    // return IGNORE_LAYOUT_IF_PARAM_EXISTS.find((qParam) => path.indexOf(qParam) != -1) != null;
    return false;
  };

  const Wrapper = PAGES_WITHOUT_LAYOUT.indexOf(currPath) != -1 || anyHideParamsExists() ? PureClientOnly : Layout;
  // const Wrapper = Layout;
  logger.log({ currPath, Wrapper });

  return <Wrapper>{children}</Wrapper>;
};

export default LayoutProvider;
