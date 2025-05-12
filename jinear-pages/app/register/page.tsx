"use client";
import RegisterWithMailForm from "@/components/form/registerWithMailForm/RegisterWithMailForm";
import FormLogo from "@/components/formLogo/FormLogo";
import React from "react";
import styles from "./index.module.scss";

interface RegisterPageProps {}

const RegisterPage: React.FC<RegisterPageProps> = ({}) => {
  return (
    <div className={styles.container}>
      <RegisterWithMailForm className={styles.form} />
      <FormLogo />
    </div>
  );
};

export default RegisterPage;
