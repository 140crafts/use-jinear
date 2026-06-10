"use client";
import React, { useState } from "react";
import styles from "./MacTerminal.module.scss";
import { LuCopy, LuCheck } from "react-icons/lu";

interface MacTerminalProps {
  command: string;
  comment?: string;
}

const MacTerminal: React.FC<MacTerminalProps> = ({
  command,
  comment = "# One command. Your whole stack, on your server.",
}) => {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(command).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    });
  };

  return (
    <div className={styles.window}>
      <div className={styles.titleBar}>
        <div className={styles.dots}>
          <span className={`${styles.dot} ${styles.red}`} />
          <span className={`${styles.dot} ${styles.yellow}`} />
          <span className={`${styles.dot} ${styles.green}`} />
        </div>
        <span className={styles.windowTitle}>Install Jinear</span>
        <div className={styles.dotsSpacer} />
      </div>
      <div className={styles.body}>
        <p className={styles.comment}>{comment}</p>
        <div className={styles.commandRow}>
          <p className={styles.commandLine}>
            <span className={styles.promptChar}>$</span>
            <span className={styles.command}> {command}</span>
          </p>
          <button className={styles.copyButton} onClick={handleCopy} title="Copy command">
            {copied
              ? <LuCheck className={styles.copyIcon} />
              : <LuCopy className={styles.copyIcon} />
            }
          </button>
        </div>
      </div>
    </div>
  );
};

export default MacTerminal;
