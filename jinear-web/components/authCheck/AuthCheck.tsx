import { useMeQuery } from "@/store/api/accountApi";
import Logger from "@/utils/logger";
import React from "react";

interface AuthCheckProps {}

const logger = Logger("AuthCheck");

const AuthCheck: React.FC<AuthCheckProps> = ({}) => {
  const { data, error, isLoading } = useMeQuery();
  logger.log({ data, error, isLoading });
  return null;
};

export default AuthCheck;
