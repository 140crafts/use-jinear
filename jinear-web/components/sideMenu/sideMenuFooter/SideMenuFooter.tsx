import Button, { ButtonVariants } from "@/components/button";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import cn from "classnames";
import React from "react";
import styles from "./SideMenuFooter.module.css";

interface SideMenuFooterProps {
  className?: string;
}

const SideMenuFooter: React.FC<SideMenuFooterProps> = ({ className }) => {
  return (
    <div className={cn(styles.container, className)}>
      <Button
        variant={ButtonVariants.hoverFilled}
        className={styles.accountButton}
      >
        Cagdas Tunca
      </Button>
      <ThemeToggle />
    </div>
  );
};

export default SideMenuFooter;
