import React from "react";
import styles from "./NewPostFile.module.css";
import { LuFile, LuX } from "react-icons/lu";
import Image from "next/image";
import Button, { ButtonVariants } from "@/components/button";

interface NewPostFileProps {
  file: File;
  onRemove: () => void;
}

const NewPostFile: React.FC<NewPostFileProps> = ({ file, onRemove }) => {
  const type = file.type || "";
  return (
    <div className={styles.container}>
      {type.indexOf("image") != -1 ?
        <Image
          alt={`New post file ${file.name}`}
          className={styles.img}
          fill={true}
          src={URL.createObjectURL(file)} /> :
        <div className={styles.logoContainer}>
          <LuFile className={styles.logo} />
          <span className={"line-clamp-2"}>
            {file.name}
          </span>
        </div>
      }
      <Button
        className={styles.closeButton}
        variant={ButtonVariants.filled}
        onClick={onRemove}
      >
        <LuX className={"icon"} />
      </Button>
    </div>
  );
};

export default NewPostFile;