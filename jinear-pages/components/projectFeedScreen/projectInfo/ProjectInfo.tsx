import React from "react";
import styles from "./ProjectInfo.module.css";
import { PublicProjectDto } from "@/be/jinear-core";
import { LuBox } from "react-icons/lu";
import Tiptap from "@/components/tiptap/Tiptap";

interface ProjectInfoProps {
  publicProject: PublicProjectDto;
}

const ProjectInfo: React.FC<ProjectInfoProps> = ({ publicProject }) => {

  return (
    <div className={styles.container}>
      {publicProject.logo &&
        <img
          alt={"Project logo"}
          src={publicProject.logo.url}
          className={styles.profilePicture}
        />
      }
      <div className={styles.infoContainer}>
        <h1 className={styles.title}>{publicProject.title}</h1>
        {publicProject.info?.value &&
          <Tiptap
            className={styles.input}
            editorClassName={styles.input}
            editorWrapperClassName={styles.editorWrapper}
            editable={false}
            hideActionBarWhenEmpty={false}
            content={publicProject.info?.value}
          />
        }
      </div>
    </div>
  );
};

export default ProjectInfo;