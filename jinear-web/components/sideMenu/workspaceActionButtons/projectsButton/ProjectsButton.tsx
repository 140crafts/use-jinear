import React from "react";
import styles from "./ProjectsButton.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { WorkspaceDto } from "@/be/jinear-core";
import { LuBox } from "react-icons/lu";
import { usePathname } from "next/navigation";

interface ProjectsButtonProps {
  workspace: WorkspaceDto;
}

const ProjectsButton: React.FC<ProjectsButtonProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const pathname = usePathname();
  const projectsPath = `/${workspace.username}/tasks/projects`;

  return (
    <Button className={styles.button}
            heightVariant={ButtonHeight.short}
            variant={pathname.indexOf(projectsPath) != -1 ? ButtonVariants.filled : ButtonVariants.default}
            href={projectsPath}
    >
      <LuBox />
      {t("sideMenuProjectsTitle")}
    </Button>
  );
};

export default ProjectsButton;