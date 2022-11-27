import cn from "classnames";
import React from "react";
import BaseModal, { BaseModalProps, ModalWidth } from "../baseModal/BaseModal";
import styles from "./Modal.module.css";

interface ModalProps extends BaseModalProps {
  visible?: boolean;
  requestClose?: () => void;
  children: any;
  contentContainerClass?: string;
  width?: ModalWidth;
  title?: string;
}

const Modal: React.FC<ModalProps> = ({
  visible = true,
  requestClose,
  children,
  contentContainerClass,
  width = "medium-fixed",
  title,
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
          <h3>{title}</h3>
        </div>
      )}
      <div className={styles.body}>{children}</div>
    </BaseModal>
  );
};

export default Modal;
