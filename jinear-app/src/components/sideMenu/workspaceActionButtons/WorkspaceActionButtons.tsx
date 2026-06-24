import React from "react";
import styles from "./WorkspaceActionButtons.module.css";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {LuClipboardPen, LuSearch, LuSparkles, LuSquarePen} from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";
import {closeSearchTaskModal, popNewTaskModal, popSearchTaskModal} from "@/slice/modalSlice";
import {useAppDispatch} from "@/store";
import type {TaskDto, WorkspaceDto} from "@/be/jinear-core";
import {useWorkspaceFirstTeam} from "@/hooks/useWorkspaceFirstTeam";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import Logger from "@/util/logger";
import {useLocation, useNavigate} from "react-router-dom";

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
    const navigate = useNavigate();

    const _popNewTaskModal = () => {
        if (workspace && team) {
            dispatch(popNewTaskModal({visible: true, workspace, team}));
        }
    };

    const navigateToTask = (task: TaskDto) => {
        if (task) {
            navigate(`/${workspace.username}/task/${task?.team?.tag}-${task.teamTagNo}`);
            dispatch(closeSearchTaskModal());
        }
    }

    const searchTask = () => {
        dispatch(popSearchTaskModal({
            workspaceId: workspace.workspaceId,
            teamIds: [],
            onSelect: navigateToTask,
            visible: true
        }))
    }

    return (
        <div className={styles.container}>
            <MenuGroupTitle label={t("sideMenuYourWorkspaceTitle")}/>
            <div className={styles.buttonsContainer}>

                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.brandColor}
                    className={styles.newTaskButton}
                    onClick={_popNewTaskModal}
                    disabled={!workspace || !team}
                >
                    <LuSquarePen/>
                    <b>{t("sideMenuNewTask")}</b>
                </Button>

                <Button
                    className={styles.button}
                    variant={ButtonVariants.hoverFilled2}
                    onClick={searchTask}
                >
                    <LuSearch className={styles.icon}/>
                    {t("mainFeaturesMenuLabelSearch")}
                </Button>

                <Button
                    className={styles.button}
                    href={lastActivitiesPath}
                    variant={pathname?.indexOf(lastActivitiesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
                >
                    <LuSparkles className={styles.icon}/>
                    {t("mainFeaturesMenuLabelLastActivities")}
                </Button>

                <Button
                    className={styles.button}
                    href={assignedToMePath}
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