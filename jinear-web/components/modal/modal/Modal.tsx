import Button from "@/components/button";
import cn from "classnames";
import React from "react";
import { IoClose } from "react-icons/io5";
import BaseModal, { BaseModalProps, ModalWidth } from "../baseModal/BaseModal";
import styles from "./Modal.module.css";

interface ModalProps extends BaseModalProps {
  visible?: boolean;
  requestClose?: () => void;
  children: any;
  contentContainerClass?: string;
  bodyClass?: string;
  width?: ModalWidth;
  title?: string;
  hasTitleCloseButton?: boolean;
}

const Modal: React.FC<ModalProps> = ({
  visible = true,
  requestClose,
  children,
  contentContainerClass,
  bodyClass,
  width = "medium-fixed",
  title,
  hasTitleCloseButton = false,
}) => {
  return (
    <BaseModal
      visible={visible}
      requestClose={requestClose}
      contentContainerClass={cn(styles.container, contentContainerClass)}
      width={width}
    >
      {title && (
        <div className={styles.titleBar}>
          {hasTitleCloseButton && (
            <div className={styles.titleBarIconButtonContainer}>
              <Button onClick={requestClose}>
                <IoClose size={18} />
              </Button>
            </div>
          )}
          <h3 className={styles.title}>{title}</h3>
          {hasTitleCloseButton && <div className={styles.titleBarIconButtonContainer} />}
        </div>
      )}
      <div className={cn(styles.body, width == "fullscreen" && styles.fullHeightBody, bodyClass)}>{children}</div>
    </BaseModal>
  );
};

export default Modal;
