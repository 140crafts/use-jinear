"use client";
import { useAccountsFirstWorkspace } from "@/hooks/useAccountsFirstWorkspace";
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
  const accountsFirstWorkspace = useAccountsFirstWorkspace();
  const preferedWorkspace = useTypedSelector((state) => state.account.current?.workspaceDisplayPreference?.workspace);
  const workspace = preferedWorkspace ? preferedWorkspace : accountsFirstWorkspace;
  useEffect(() => {
    if (workspace?.username) {
      router.replace(`/${workspace?.username}`);
    }
  }, [workspace?.username]);

  return (
    <div className={styles.container}>
      <div>{t("homeScreenLoadingWorkspaceText")}</div>
      <div className="spacer-h-2" />
      <CircularProgress size={24} />
      <div className="spacer-h-2" />
      <i>{workspace?.username}</i>
    </div>
  );
};

export default HomePage;
