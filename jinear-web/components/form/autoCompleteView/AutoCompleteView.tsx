import useActiveElement from "@/hooks/useActiveElement";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { AnimatePresence, motion } from "framer-motion";
import React, { useState } from "react";
import styles from "./AutoCompleteView.module.css";

interface AutoCompleteViewProps {
  children: React.ReactNode;
  forElement: string;
}

const AutoCompleteView: React.FC<AutoCompleteViewProps> = ({ children, forElement }) => {
  const { activeElement, listenersReady } = useActiveElement();
  const [activeEl, setActiveEl] = useState(activeElement);
  const isPresent = activeEl?.id == forElement;

  const animations = {
    layout: false,
    initial: "out",
    animate: isPresent ? "in" : "out",
    variants: {
      in: {
        opacity: 1,
      },
      out: {
        opacity: 0,
        transition: { duration: 0 },
      },
    },
    transition: { type: "spring", stiffness: 500, damping: 50, mass: 2 },
    onAnimationComplete: () => !isPresent,
  };

  useDebouncedEffect(() => setActiveEl(activeElement), [activeElement], 250);

  return (
    <AnimatePresence initial={false}>
      <motion.div
        key={`auto-complete-view-for-${forElement}`}
        id={`auto-complete-view-for-${forElement}`}
        className={styles.autoCompleteContainer}
        {...animations}
        style={{
          display: isPresent ? "flex" : "none",
          position: "absolute",
          top: (activeEl?.offsetTop || 0) + (activeEl?.offsetHeight || 0) - 1,
        }}
      >
        {children}
      </motion.div>
    </AnimatePresence>
  );
};

export default AutoCompleteView;
