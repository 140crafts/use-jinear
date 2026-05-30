import React from "react";
import styles from "./ProjectPriorityIcon.module.css";
import {LuTriangleAlert, LuSquareDashed, LuSignal, LuSignalHigh, LuSignalLow, LuSignalMedium} from "react-icons/lu";
import type {ProjectPriorityType} from "@/be/jinear-core";
import cn from "classnames";

interface ProjectPriorityIconProps {
    projectPriority: ProjectPriorityType;
    className?: string;
    withColor?: boolean;
    size?: number;
}

const ICON_MAP = {
    NONE: LuSquareDashed,
    URGENT: LuTriangleAlert,
    HIGH: LuSignalHigh,
    MEDIUM: LuSignalMedium,
    LOW: LuSignalLow
};

const ProjectPriorityIcon: React.FC<ProjectPriorityIconProps> = ({
                                                                     projectPriority,
                                                                     withColor = false,
                                                                     className,
                                                                     size
                                                                 }) => {
    const Icon = ICON_MAP[projectPriority] ?? LuSignal;

    return (
        <Icon size={size} className={cn(styles.icon, withColor && styles[projectPriority], className)}/>
    );
};

export default ProjectPriorityIcon;