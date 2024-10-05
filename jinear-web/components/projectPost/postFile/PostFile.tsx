import React from "react";
import styles from "./PostFile.module.css";
import { AccessibleMediaDto } from "@/be/jinear-core";
import Image from "next/image";
import { LuFile } from "react-icons/lu";

interface PostFileProps {
  file: AccessibleMediaDto;
}

const PostFile: React.FC<PostFileProps> = ({ file }) => {
  const type = file.contentType || "";

  const onClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e?.preventDefault?.();
    window.open(file.url, "_blank");
  };

  return (
    <button className={styles.container} onClick={onClick}>
      {type.indexOf("image") != -1 ?
        <Image
          alt={`Post file ${file.originalName}`}
          className={styles.img}
          fill={true}
          src={file.url} /> :
        type.indexOf("video") != -1 ?
          <video
            className={styles.video}
            src={file.url}>
          </video> :
          <div className={styles.logoContainer}>
            <LuFile className={styles.logo} />
            <span className={"line-clamp-2"}>
            {file.originalName}
          </span>
          </div>
      }
    </button>
  );
};

export default PostFile;