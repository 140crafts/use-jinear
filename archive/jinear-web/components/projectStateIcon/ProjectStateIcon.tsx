import React from "react";
import styles from "./ProjectStateIcon.module.css";
import { ProjectStateType } from "@/be/jinear-core";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import cn from "classnames";

interface ProjectStateIconProps {
  projectState: ProjectStateType;
  className?: string;
  size?: number;
}

const ICON_MAP = {
  BACKLOG: IoPauseCircleOutline,
  PLANNED: IoEllipseOutline,
  IN_PROGRESS: IoContrast,
  COMPLETED: IoCheckmarkCircle,
  CANCELLED: IoCloseCircle
};

const ProjectStateIcon: React.FC<ProjectStateIconProps> = ({
                                                             projectState,
                                                             className,
                                                             size
                                                           }) => {
  const Icon = ICON_MAP[projectState] ?? IoEllipseOutline;

  return (
    <Icon size={size} className={cn(styles.icon, className)} />
  );
};

export default ProjectStateIcon;