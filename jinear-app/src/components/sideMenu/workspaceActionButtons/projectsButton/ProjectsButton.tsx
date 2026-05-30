import React from "react";
import styles from "./ProjectsButton.module.css";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import type {WorkspaceDto} from "@/be/jinear-core";
import {LuBox} from "react-icons/lu";

import Logger from "@/util/logger";
import {useLocation} from "react-router-dom";

interface ProjectsButtonProps {
    workspace: WorkspaceDto;
}

const logger = Logger("ProjectsButton");

const ProjectsButton: React.FC<ProjectsButtonProps> = ({workspace}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();
    const projectsPath = `/${workspace.username}/tasks/projects`;

    logger.log({pathname, projectsPath});

    return (
        <Button className={styles.button}
                heightVariant={ButtonHeight.short}
                variant={pathname.indexOf(projectsPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                href={projectsPath}
        >
            <LuBox/>
            {t("sideMenuProjectsTitle")}
        </Button>
    );
};

export default ProjectsButton;