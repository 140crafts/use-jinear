import React from "react";
import styles from "./WorkspaceActionButtons.module.css";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuClipboardPen, LuSparkles, LuSquarePen} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import {popNewTaskModal} from "@/slice/modalSlice";
import {useAppDispatch} from "@/store";
import type {WorkspaceDto} from "@/be/jinear-core";
import {useWorkspaceFirstTeam} from "@/hooks/useWorkspaceFirstTeam";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import Logger from "@/util/logger";
import {useLocation} from "react-router-dom";

interface WorkspaceActionButtonsProps {
    workspace: WorkspaceDto;
}

const logger = Logger("WorkspaceActionButtons");
const WorkspaceActionButtons: React.FC<WorkspaceActionButtonsProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const team = useWorkspaceFirstTeam(workspace.workspaceId);
    const {pathname} = useLocation();
    const assignedToMePath = `/${workspace?.username}/tasks/assigned-to-me`;
    const lastActivitiesPath = `/${workspace?.username}/tasks/last-activities`;

    const _popNewTaskModal = () => {
        if (workspace && team) {
            dispatch(popNewTaskModal({visible: true, workspace, team}));
        }
    };

    return (
        <div className={styles.container}>
            <MenuGroupTitle label={t("sideMenuYourWorkspaceTitle")}/>
            <div className={styles.buttonsContainer}>

                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.brandColor}
                    className={styles.newTaskButton}
                    onClick={_popNewTaskModal}
                    shortcut="newTask"
                    disabled={!workspace || !team}
                >
                    <LuSquarePen/>
                    <b>{t("sideMenuNewTask")}</b>
                </Button>

                <Button
                    className={styles.button}
                    href={lastActivitiesPath}
                    shortcut="lastActivities"
                    variant={pathname?.indexOf(lastActivitiesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuSparkles className={styles.icon}/>
                    {t("mainFeaturesMenuLabelLastActivities")}
                </Button>

                <Button
                    className={styles.button}
                    href={assignedToMePath}
                    shortcut="assignedToMe"
                    variant={pathname?.indexOf(assignedToMePath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuClipboardPen className={styles.icon}/>
                    {t("mainFeaturesMenuLabelAssignedToMe")}
                </Button>

            </div>
        </div>
    );
};

export default WorkspaceActionButtons;