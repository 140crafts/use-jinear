"use client";
import cn from "classnames";
import { AnimatePresence, motion, usePresence } from "framer-motion";
import React from "react";
import Button from "../button";
import styles from "./FormLogo.module.scss";

interface FormLogoProps {
  withLeftLine?: boolean;
  contentClassName?: string;
}

const FormLogo: React.FC<FormLogoProps> = ({ withLeftLine = true, contentClassName }) => {
  const [isPresent, safeToRemove] = usePresence();

  const animations = {
    layout: false,
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        marginRight: 0,
        opacity: 1,
        transition: { duration: 0.75 }
      },
      out: {
        marginRight: -35,
        opacity: 0
      }
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent && safeToRemove?.()
  };

  return (
    <div className={styles.logoContainer}>
      {withLeftLine && <div className={styles.logoLine}></div>}
      <div className={cn(styles.logoContent, contentClassName)}>
        <AnimatePresence>
          <motion.div
            {...animations}
            className={styles.logoText}
            style={{
              position: isPresent ? "static" : "absolute"
            }}
          >
            <Button href="/" className={styles.logoButton}>
              JINEAR
            </Button>
          </motion.div>
        </AnimatePresence>
      </div>
    </div>
  );
};

export default FormLogo;
