import ForgotPasswordForm from "@/components/form/forgotPasswordForm/ForgotPasswordForm";
import FormLogo from "@/components/formLogo/FormLogo";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface ForgotPasswordPageProps {}

const ForgotPasswordPage: React.FC<ForgotPasswordPageProps> = ({}) => {
  const router = useRouter();
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);

  useEffect(() => {
    if (isLoggedIn) {
      router.replace("/");
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
