import { AnimatePresence, motion, usePresence } from "framer-motion";
import React from "react";
import styles from "./FormLogo.module.scss";

interface FormLogoProps {}

const FormLogo: React.FC<FormLogoProps> = ({}) => {
  const [isPresent, safeToRemove] = usePresence();

  const animations = {
    layout: false,
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        marginRight: 0,
        opacity: 1,
        transition: { duration: 0.75 },
      },
      out: {
        marginRight: -35,
        opacity: 0,
      },
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent && safeToRemove?.(),
  };

  return (
    <div className={styles.logoContainer}>
      <div className={styles.logoLine}></div>
      <div className={styles.logoContent}>
        <div className={styles.logoImg}></div>
        <AnimatePresence>
          <motion.div
            {...animations}
            className={styles.logoText}
            style={{
              position: isPresent ? "static" : "absolute",
            }}
          >
            Jinear
          </motion.div>
        </AnimatePresence>
      </div>
    </div>
  );
};

export default FormLogo;
