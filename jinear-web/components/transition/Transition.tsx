import cn from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import { useRouter } from "next/router";
import React from "react";
import styles from "./Transition.module.css";

interface TransitionProps {
  initial?: boolean;
  className?: string;
  children: React.ReactNode;
}

const Transition: React.FC<TransitionProps> = ({
  initial = false,
  className,
  children,
}) => {
  const router = useRouter();
  const currPath = router?.asPath?.split("#")[0]?.split("?")?.[0];

  const variants = {
    out: {
      opacity: 0,
      transition: {
        duration: 0,
      },
    },
    in: {
      opacity: 1,
      transition: {
        duration: 0.25,
      },
    },
  };
  return (
    <AnimatePresence initial={initial} exitBeforeEnter>
      <motion.div
        key={currPath}
        variants={variants}
        animate="in"
        initial="out"
        exit="out"
        className={cn(styles.contaniner, className)}
      >
        {children}
      </motion.div>
    </AnimatePresence>
  );
};

export default Transition;
