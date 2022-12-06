import { useMeQuery } from "@/store/api/accountApi";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React from "react";

interface AuthCheckProps {}

const logger = Logger("AuthCheck");

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
  const { data, error, isLoading } = useMeQuery();
  const authState = useTypedSelector(selectAuthState);
  logger.log({ data, error, isLoading, authState });
  return null;
};

export default AuthCheck;
