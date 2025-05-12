"use client";
import LoginWithMailForm from "@/components/form/loginWithMailForm/LoginWithMailForm";
import FormLogo from "@/components/formLogo/FormLogo";
import Logger from "@/utils/logger";
import { useSearchParams } from "next/navigation";
import React from "react";
import styles from "./index.module.scss";

interface LoginPageProps {}

const logger = Logger("LoginPage");

const LoginPage: React.FC<LoginPageProps> = ({}) => {
  logger.log("LoginPage");
  const params = useSearchParams();
  const email = params?.get("email");

  return (
    <div className={styles.container}>
      <LoginWithMailForm className={styles.form} initialEmail={email as string | undefined} />
      <FormLogo />
    </div>
  );
};

export default LoginPage;
