import React, { useRef, useState } from "react";
import styles from "./DropZone.module.css";

interface DropZoneProps {
  children: React.ReactNode;
  onDrop: ({ files }: { files: File[] }) => void;
  disabled?: boolean;
  overlayText?: string;
}

const DropZone: React.FC<DropZoneProps> = ({
                                             children,
                                             onDrop,
                                             disabled = false,
                                             overlayText = "Drop files to upload"
                                           }) => {
  const [isDragging, setIsDragging] = useState(false);
  const dragCounter = useRef(0);

  const handleDragEnter = (e: React.DragEvent) => {
    e.preventDefault();
    if (disabled) return;
    dragCounter.current++;
    if (e.dataTransfer.types.includes("Files")) {
      setIsDragging(true);
    }
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    dragCounter.current--;
    if (dragCounter.current === 0) {
      setIsDragging(false);
    }
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    dragCounter.current = 0;
    setIsDragging(false);
    if (disabled) return;

    const files = Array.from(e.dataTransfer.files);
    if (files.length > 0) {
      onDrop({ files });
    }
  };

  return (
    <div
      className={styles.container}
      onDragEnter={handleDragEnter}
      onDragLeave={handleDragLeave}
      onDragOver={handleDragOver}
      onDrop={handleDrop}
    >
      {children}
      {isDragging && (
        <div className={styles.overlay}>
          <div className={styles.overlayContent}>
            <span className={styles.overlayText}>{overlayText}</span>
          </div>
        </div>
      )}
    </div>
  );
};

export default DropZone;