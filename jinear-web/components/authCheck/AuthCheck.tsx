"use client";
import { useMeQuery } from "@/store/api/accountApi";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { askAppTrackingPermission } from "@/utils/webviewUtils";
import { usePathname, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { deleteAllIndexedDbs } from "../../repository/IndexedDbRepository";

interface AuthCheckProps {
}

const logger = Logger("AuthCheck");

const PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS = [
  "/engage/confirm-email",
  "/engage/reset-password-complete",
  "/engage/forgot-password",
  "/engage/workspace-invitation",
  "/pricing",
  "/terms",
  "/debug",
  "/project-feed/"
];

const ONLY_NOT_LOGGED_IN_PATHS = ["/forgot-password", "/register", "/login"];

const checkPathIsIn = (pathname: string, allowed: string[]) => {
  const result = allowed?.find(allowedPath => allowedPath.indexOf(pathname)) != null;
  logger.log({ pathname, allowed, checkPathIsIn: result });
  return result;
};

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
  const dispatch = useAppDispatch();
  const { data, error, isLoading } = useMeQuery();
  const router = useRouter();
  const pathname = usePathname() || "";
  const authState = useTypedSelector(selectAuthState);
  logger.log({
    authState,
    pathname,
    activeAccount: { data, error, isLoading }
  });

  useEffect(() => {
    if (authState == "LOGGED_IN" && ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) != -1) {
      logger.log("This path only for NOT LOGGED IN users.");
      router.replace("/");
    } else if (
      authState == "NOT_LOGGED_IN" &&
      ONLY_NOT_LOGGED_IN_PATHS.indexOf(pathname) == -1 &&
      !checkPathIsIn(pathname, PATHS_EVERYONE_CAN_VISIT_INREGARD_OF_THEIR_LOGIN_STATUS)
    ) {
      logger.log("This path only for LOGGED IN users.");
      router.replace("/");
    }
    if (authState == "LOGGED_IN") {
      askAppTrackingPermission();
    }
    if (authState === "NOT_LOGGED_IN") {
      // resetAllStates(dispatch);
      deleteAllIndexedDbs();
    }
  }, [router, dispatch, authState, pathname]);

  return null;
};

export default AuthCheck;
