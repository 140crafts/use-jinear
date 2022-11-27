import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React from "react";
import { PureClientOnly } from "../clientOnly/ClientOnly";
import Layout from "./layout/Layout";

interface LayoutProviderProps {
  children: React.ReactNode;
  navbar?: boolean;
  tabbar?: boolean;
}

const PAGES_WITHOUT_LAYOUT = [
  "/meeting/[bookingId]",
  "/meeting/quick/[bookingId]",
];

const IGNORE_LAYOUT_IF_PARAM_EXISTS = ["forPrint=true"];

const logger = Logger("LayoutProvider");
const LayoutProvider: React.FC<LayoutProviderProps> = ({ children }) => {
  const router = useRouter();
  const currPath = router.pathname;

  const anyHideParamsExists = () => {
    const path = router?.asPath;
    return (
      IGNORE_LAYOUT_IF_PARAM_EXISTS.find(
        (qParam) => path.indexOf(qParam) != -1
      ) != null
    );
  };

  const Wrapper =
    PAGES_WITHOUT_LAYOUT.indexOf(currPath) != -1 || anyHideParamsExists()
      ? PureClientOnly
      : Layout;
  logger.log({ currPath, Wrapper });
  return (
    <Wrapper navbar={true} tabbar={true}>
      {children}
    </Wrapper>
  );
};

export default LayoutProvider;
