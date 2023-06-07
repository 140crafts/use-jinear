import cn from "classnames";
import { AnimatePresence, motion } from "framer-motion";
import React from "react";
import styles from "./style.module.css";

export type ModalWidth = "medium-fixed" | "default" | "large" | "xlarge" | "xxlarge" | "fullscreen";
export type ModalHeight = "default" | "height-medium-or-full";

export interface BaseModalProps {
  visible?: boolean;
  requestClose?: () => void;
  children: any;
  contentContainerClass?: string;
  contentClassName?: string;
  closepadClassName?: string;
  width?: ModalWidth;
  height?: ModalHeight;
}

const BaseModal: React.FC<BaseModalProps> = ({
  visible = true,
  requestClose,
  children,
  contentContainerClass,
  contentClassName,
  closepadClassName,
  width = "default",
  height = "default",
}) => {
  const avoid = () => {};

  return (
    <AnimatePresence initial={false} exitBeforeEnter={true}>
      {visible && (
        <div className={cn(styles.container, styles[`${width}-container`])}>
          <div
            className={cn([styles.content, styles[`${width}-content`], styles[`${height}-content`], contentClassName])}
            onClick={avoid}
          >
            <motion.div
              className={cn(styles.children, contentContainerClass)}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0, transition: { duration: 0.35 } }}
            >
              {children}
            </motion.div>
          </div>
          <motion.div
            onClick={requestClose}
            className={cn([styles.closepad, closepadClassName])}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          />
        </div>
      )}
    </AnimatePresence>
  );
};

export default BaseModal;
