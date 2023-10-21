"use client";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface HomePageProps {}

const HomePage: React.FC<HomePageProps> = ({}) => {
  const router = useRouter();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  useEffect(() => {
    if (currentWorkspace?.username) {
      router.replace(`/${currentWorkspace?.username}`);
    }
  }, [currentWorkspace?.username]);

  return (
    <div className={styles.container}>
      {currentWorkspace?.username}
      <CircularProgress size={24} />
    </div>
  );
};

export default HomePage;
