import { useMeQuery } from "@/store/api/accountApi";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

interface AuthCheckProps {}

const logger = Logger("AuthCheck");

const PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS = [
  "/engage/[token]/confirm-email",
  "/engage/[token]/reset-password-complete",
  "/engage/[token]/forgot-password",
];

const ONLY_NOT_LOGGED_IN_PATHS = ["/forgot-password", "/register", "/login"];

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
  const { data, error, isLoading } = useMeQuery();
  const router = useRouter();
  const pathname = router.pathname;
  const authState = useTypedSelector(selectAuthState);
  logger.log({
    authState,
    pathname,
    activeAccount: { data, error, isLoading },
  });

  useEffect(() => {
    if (
      authState == "LOGGED_IN" &&
      ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) != -1
    ) {
      logger.log("This path only for NOT LOGGED IN users.");
      router.replace("/");
    } else if (
      authState == "NOT_LOGGED_IN" &&
      ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) == -1 &&
      PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS.indexOf(
        pathname
      ) == -1
    ) {
      logger.log("This path only for LOGGED IN users.");
      router.replace("/");
    }
  }, [authState, pathname]);

  return null;
};

export default AuthCheck;
