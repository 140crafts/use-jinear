import { selectAuthState, selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";

interface OnboardListenerProps {}

const logger = Logger("OnboardListener");

const OnboardListener: React.FC<OnboardListenerProps> = ({}) => {
  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const currentAccountWorkspaces = useTypedSelector(selectCurrentAccountsWorkspaces);

  useEffect(() => {
    logger.log({ authState, currentAccountWorkspaces });
    if (authState == "LOGGED_IN" && currentAccountWorkspaces && currentAccountWorkspaces.length == 0) {
      router.replace("/new-workspace");
      return;
    }
  }, [authState, currentAccountWorkspaces]);

  return null;
};

export default OnboardListener;
