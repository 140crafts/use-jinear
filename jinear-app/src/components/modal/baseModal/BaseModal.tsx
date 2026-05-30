import cn from "classnames";
import React from "react";
import styles from "./style.module.css";

export type ModalWidth = "small" | "medium-fixed" | "default" | "large" | "xlarge" | "xxlarge" | "fullscreen";
export type ModalHeight = "default" | "height-medium-or-full" | "height-full";

export interface BaseModalProps {
  visible?: boolean;
  requestClose?: () => void;
  children: any;
  containerClassName?: string;
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
                                               containerClassName,
                                               contentContainerClass,
                                               contentClassName,
                                               closepadClassName,
                                               width = "default",
                                               height = "default"
                                             }) => {
  const avoid = () => {
  };

  return (
    visible ? (
      <div className={cn(styles.container, styles[`${width}-container`], containerClassName)}>
        <div
          className={cn([styles.content, styles[`${width}-content`], styles[`${height}-content`], contentClassName])}
          onClick={avoid}
        >
          <div
            className={cn(styles.children, contentContainerClass)}
          >
            {children}
          </div>
        </div>
        <div
          onClick={requestClose}
          className={cn([styles.closepad, closepadClassName])}
        />
      </div>
    ) : null
  );
};

export default BaseModal;
