import { useMeQuery } from "@/store/api/accountApi";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

interface AuthCheckProps {}

const logger = Logger("AuthCheck");

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
  const router = useRouter();
  const { data, error, isLoading } = useMeQuery();
  const authState = useTypedSelector(selectAuthState);

  logger.log({ data, error, isLoading, authState });

  useEffect(() => {
    if (authState == "LOGGED_IN") {
      router.replace(ROUTE_IF_LOGGED_IN);
    } else if (authState == "NOT_LOGGED_IN") {
      router.replace("/login");
    }
  }, [authState]);

  return null;
};

export default AuthCheck;
