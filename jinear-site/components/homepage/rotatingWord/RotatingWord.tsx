"use client";
import React, { useEffect, useState } from "react";
import styles from "./RotatingWord.module.scss";

interface RotatingWordProps {
  /** The first word is the one pre-rendered into the static HTML. */
  words: string[];
  intervalMs?: number;
}

const RotatingWord: React.FC<RotatingWordProps> = ({ words, intervalMs = 2400 }) => {
  const [index, setIndex] = useState(0);

  useEffect(() => {
    if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
      return;
    }
    const timer = setInterval(() => setIndex((i) => (i + 1) % words.length), intervalMs);
    return () => clearInterval(timer);
  }, [words.length, intervalMs]);

  return (
    <span className={styles.slot}>
      <span key={index} className={styles.word}>
        {words[index]}
      </span>
    </span>
  );
};

export default RotatingWord;
