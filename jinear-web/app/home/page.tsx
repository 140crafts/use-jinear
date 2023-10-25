"use client";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface HomePageProps {}

const HomePage: React.FC<HomePageProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  useEffect(() => {
    if (currentWorkspace?.username) {
      router.replace(`/${currentWorkspace?.username}`);
    }
  }, [currentWorkspace?.username]);

  return (
    <div className={styles.container}>
      <div>{t("homeScreenLoadingWorkspaceText")}</div>
      <div className="spacer-h-2" />
      <CircularProgress size={24} />
      <div className="spacer-h-2" />
      <i>{currentWorkspace?.username}</i>
    </div>
  );
};

export default HomePage;
