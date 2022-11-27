import LoginWithMailForm from "@/components/form/loginWithMailForm/LoginWithMailForm";
import FormLogo from "@/components/formLogo/FormLogo";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface LoginPageProps {}

const logger = Logger("LoginPage");

const LoginPage: React.FC<LoginPageProps> = ({}) => {
  logger.log("LoginPage");
  const router = useRouter();
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);
  const { email } = router.query;

  useEffect(() => {
    if (isLoggedIn) {
      router.replace("/");
    }
  }, [isLoggedIn]);

  return (
    <div className={styles.container}>
      <LoginWithMailForm
        className={styles.form}
        initialEmail={email as string | undefined}
      />
      <FormLogo />
    </div>
  );
};

export default LoginPage;
