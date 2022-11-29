import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import React from "react";
import styles from "./index.module.scss";

interface HomePageProps {}

const HomePage: React.FC<HomePageProps> = ({}) => {
  return (
    <div className={styles.container}>
      HomePage
      <ThemeToggle />
    </div>
  );
};

export default HomePage;
