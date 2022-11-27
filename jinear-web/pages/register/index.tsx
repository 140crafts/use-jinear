import RegisterWithMailForm from "@/components/form/registerWithMailForm/RegisterWithMailForm";
import FormLogo from "@/components/formLogo/FormLogo";
import { selectIsLoggedIn } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface RegisterPageProps {}

const RegisterPage: React.FC<RegisterPageProps> = ({}) => {
  const router = useRouter();
  const isLoggedIn = useTypedSelector(selectIsLoggedIn);

  useEffect(() => {
    if (isLoggedIn) {
      router.replace("/");
    }
  }, [isLoggedIn]);

  return (
    <div className={styles.container}>
      <RegisterWithMailForm className={styles.form} />
      <FormLogo />
    </div>
  );
};

export default RegisterPage;
