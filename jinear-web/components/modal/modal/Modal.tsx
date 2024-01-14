import Button from "@/components/button";
import cn from "classnames";
import React from "react";
import { IoClose } from "react-icons/io5";
import BaseModal, { BaseModalProps, ModalHeight, ModalWidth } from "../baseModal/BaseModal";
import styles from "./Modal.module.css";

interface ModalProps extends BaseModalProps {
  visible?: boolean;
  requestClose?: () => void;
  onTitleCloeButtonClick?: () => void;
  children: any;
  containerClassName?: string;
  contentContainerClass?: string;
  bodyClass?: string;
  width?: ModalWidth;
  height?: ModalHeight;
  title?: string;
  hasTitleCloseButton?: boolean;
}

const Modal: React.FC<ModalProps> = ({
  visible = true,
  requestClose,
  children,
  containerClassName,
  contentContainerClass,
  bodyClass,
  width = "medium-fixed",
  height = "default",
  title,
  hasTitleCloseButton = false,
  onTitleCloeButtonClick,
}) => {
  const titleCloseClick = () => {
    if (onTitleCloeButtonClick) {
      onTitleCloeButtonClick?.();
      return;
    }
    requestClose?.();
  };

  return (
    <BaseModal
      visible={visible}
      requestClose={requestClose}
      contentContainerClass={cn(styles.container, contentContainerClass)}
      containerClassName={containerClassName}
      width={width}
      height={height}
    >
      {title && (
        <div className={styles.titleBar}>
          {hasTitleCloseButton && (
            <div className={styles.titleBarIconButtonContainer}>
              <Button onClick={titleCloseClick}>
                <IoClose size={18} />
              </Button>
            </div>
          )}
          <h3 className={cn(styles.title, "single-line")}>{title}</h3>
          {hasTitleCloseButton && <div className={styles.titleBarIconButtonContainer} />}
        </div>
      )}
      <div
        className={cn(styles.body, width == "fullscreen" && styles.fullHeightBody, styles[`body-height-${height}`], bodyClass)}
      >
        {children}
      </div>
    </BaseModal>
  );
};

export default Modal;
