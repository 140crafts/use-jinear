"use client";
import ForgotPasswordForm from "@/components/form/forgotPasswordForm/ForgotPasswordForm";
import FormLogo from "@/components/formLogo/FormLogo";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface ForgotPasswordPageProps {}

const ForgotPasswordPage: React.FC<ForgotPasswordPageProps> = ({}) => {
  const router = useRouter();
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);

  useEffect(() => {
    if (isLoggedIn) {
      router.replace(ROUTE_IF_LOGGED_IN);
    }
  }, [isLoggedIn]);

  return (
    <div className={styles.container}>
      <ForgotPasswordForm className={styles.form} />
      <FormLogo />
    </div>
  );
};

export default ForgotPasswordPage;
