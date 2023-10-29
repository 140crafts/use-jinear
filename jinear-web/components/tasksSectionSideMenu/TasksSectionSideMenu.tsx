"use client";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import BasicTeamList from "../sideMenu/basicTeamList/BasicTeamList";
import styles from "./TasksSectionSideMenu.module.css";

interface TasksSectionSideMenuProps {}

const TasksSectionSideMenu: React.FC<TasksSectionSideMenuProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const params = useParams();
  const workspaceName = (params?.workspaceName as string) || "";
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data?.find((team) => team);

  const _popNewTaskModal = () => {
    if (workspace) {
      dispatch(popNewTaskModal({ visible: true, workspace, team }));
    }
  };

  return (
    // <Transition className={styles.container}>
    <div className={styles.container}>
      {workspace && team && (
        <>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.filled}
            className={styles.newTaskButton}
            onClick={_popNewTaskModal}
          >
            <div>{t("sideMenuNewTask")}</div>
          </Button>
          <BasicTeamList />
        </>
      )}
    </div>
    // </Transition>
  );
};

export default TasksSectionSideMenu;
