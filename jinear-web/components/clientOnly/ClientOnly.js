"use client";
import Logger from "@/utils/logger";
import React from "react";

const logger = Logger("ClientOnly");

function ClientOnly({ children, ...delegated }) {
  const [hasMounted, setHasMounted] = React.useState(false);
  logger.log("CLIENTONLY");
  React.useEffect(() => {
    logger.log("CLIENTONLY-USEF");
    setHasMounted(true);
  }, []);
  if (!hasMounted) {
    return null;
  }
  return <div {...delegated}>{children}</div>;
}

export default ClientOnly;

export function PureClientOnly({ children }) {
  const [hasMounted, setHasMounted] = React.useState(false);
  React.useEffect(() => {
    setHasMounted(true);
  }, []);
  if (!hasMounted) {
    return null;
  }
  return children;
}
