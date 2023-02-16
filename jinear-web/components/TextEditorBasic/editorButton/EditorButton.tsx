import Button, { ButtonVariants } from "@/components/button";
import React from "react";
import styles from "./EditorButton.module.css";

interface EditorButtonProps {
  cmd: string;
  arg?: string;
  label: string;
}

const EditorButton: React.FC<EditorButtonProps> = ({ cmd, arg, label }) => {
  const onMouseDown = (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    event.preventDefault();
    document.execCommand(cmd, false, arg);
  };

  return (
    <Button
      variant={ButtonVariants.hoverFilled}
      key={cmd}
      onMouseDown={onMouseDown}
      className={styles.container}
    >
      {label}
    </Button>
  );
};

export default EditorButton;
