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
  bottomSheet?: boolean;
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
                                               height = "default",
                                               bottomSheet = false
                                             }) => {
  const avoid = () => {
  };

  // Keep the modal mounted while the bottom-sheet exit animation plays.
  const [rendered, setRendered] = React.useState(visible);
  const [closing, setClosing] = React.useState(false);

  React.useEffect(() => {
    if (visible) {
      setClosing(false);
      setRendered(true);
    } else if (bottomSheet) {
      // Defer unmount until the slide-down animation ends (see handleAnimationEnd).
      setClosing(true);
    } else {
      setRendered(false);
    }
  }, [visible, bottomSheet]);

  const handleAnimationEnd = (event: React.AnimationEvent<HTMLDivElement>) => {
    // Ignore animation events bubbling up from child content.
    if (closing && event.target === event.currentTarget) {
      setClosing(false);
      setRendered(false);
    }
  };

  return (
    rendered ? (
      <div className={cn(styles.container, styles[`${width}-container`], containerClassName)}>
        <div
          className={cn([styles.content, styles[`${width}-content`], styles[`${height}-content`], bottomSheet && (closing ? styles.bottomSheetClosing : styles.bottomSheet), contentClassName])}
          onClick={avoid}
          onAnimationEnd={handleAnimationEnd}
        >
          <div
            className={cn(styles.children, contentContainerClass)}
          >
            {children}
          </div>
        </div>
        <div
          onClick={requestClose}
          className={cn([styles.closepad, closing && styles.closepadClosing, closepadClassName])}
        />
      </div>
    ) : null
  );
};

export default BaseModal;
