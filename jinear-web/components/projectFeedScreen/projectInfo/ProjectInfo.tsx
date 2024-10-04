import React from "react";
import styles from "./ProjectInfo.module.css";
import { PublicProjectDto } from "@/be/jinear-core";
import { LuBox } from "react-icons/lu";

interface ProjectInfoProps {
  publicProject: PublicProjectDto;
}

const ProjectInfo: React.FC<ProjectInfoProps> = ({ publicProject }) => {

  return (
    <div className={styles.container}>
      <LuBox size={28} />
      <h2>{publicProject.title}</h2>
    </div>
  );
};

export default ProjectInfo;