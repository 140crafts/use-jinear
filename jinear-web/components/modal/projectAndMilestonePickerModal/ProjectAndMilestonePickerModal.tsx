import React, { useEffect, useState } from "react";
import styles from "./ProjectAndMilestonePickerModal.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Modal from "@/components/modal/modal/Modal";
import {
  closeProjectAndMilestonePickerModal,
  popNewMilestoneModal,
  popNewProjectModal,
  selectProjectAndMilestonePickerModalInitialMilestone,
  selectProjectAndMilestonePickerModalInitialProject,
  selectProjectAndMilestonePickerModalOnPick,
  selectProjectAndMilestonePickerModalOnUnpick,
  selectProjectAndMilestonePickerModalVisible,
  selectProjectAndMilestonePickerModalWorkspaceId
} from "@/slice/modalSlice";
import { useAllProjectsQuery } from "@/api/projectQueryApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { MilestoneDto, ProjectDto } from "@/be/jinear-core";
import { LuBox, LuDiamond, LuX } from "react-icons/lu";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { selectCurrentAccountsWorkspace } from "@/slice/accountSlice";

interface ProjectAndMilestonePickerModalProps {

}

const ProjectAndMilestonePickerModal: React.FC<ProjectAndMilestonePickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const visible = useTypedSelector(selectProjectAndMilestonePickerModalVisible);
  const workspaceId = useTypedSelector(selectProjectAndMilestonePickerModalWorkspaceId);
  const initialProject = useTypedSelector(selectProjectAndMilestonePickerModalInitialProject);
  const initialMilestone = useTypedSelector(selectProjectAndMilestonePickerModalInitialMilestone);
  const onPick = useTypedSelector(selectProjectAndMilestonePickerModalOnPick);
  const onUnpick = useTypedSelector(selectProjectAndMilestonePickerModalOnUnpick);

  const workspace = useTypedSelector(selectCurrentAccountsWorkspace(workspaceId));

  const [selectedProject, setSelectedProject] = useState<ProjectDto | null>();
  const [selectedMilestone, setSelectedMilestone] = useState<MilestoneDto | null>();
  const selectedProjectId = selectedProject?.projectId;

  const {
    data: allProjectsResponse,
    isFetching: isAllProjectsFetching
  } = useAllProjectsQuery({ workspaceId: workspaceId ?? "" }, { skip: workspaceId == null });

  useEffect(() => {
    setSelectedProject(initialProject);
  }, [initialProject]);

  useEffect(() => {
    setSelectedMilestone(initialMilestone);
  }, [initialMilestone]);

  useEffect(() => {
    if (allProjectsResponse && selectedProjectId) {
      const updated = allProjectsResponse?.data?.content?.find(p => p.projectId == selectedProjectId);
      setSelectedProject(updated);
    }
  }, [allProjectsResponse, selectedProjectId, setSelectedProject]);

  useEffect(() => {
    if (!visible) {
      setSelectedProject(undefined);
      setSelectedMilestone(undefined);
    }
  }, [visible]);

  const close = () => {
    dispatch(closeProjectAndMilestonePickerModal());
  };

  const deselectProject = () => {
    setSelectedProject(undefined);
    setSelectedMilestone(undefined);
  };

  const deselectMilestone = () => {
    setSelectedMilestone(undefined);
  };

  const selectMilestone = (milestone: MilestoneDto) => {
    setSelectedMilestone(milestone);
    if (selectedProject) {
      onPick?.({ project: selectedProject, milestone });
      close?.();
    }
  };

  const openNewMilestoneModal = () => {
    if (selectedProject) {
      dispatch(popNewMilestoneModal({ project: selectedProject, visible: true }));
    }
  };

  const openNewProjectModal = () => {
    dispatch(popNewProjectModal({ workspace, visible: true }));
    close();
  };

  const unpickClick = () => {
    onUnpick?.(true, true);
    close();
  };

  return (
    <Modal
      visible={visible}
      title={t("projectAndMilestonePickerModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {isAllProjectsFetching && <CircularLoading />}

      {!isAllProjectsFetching && !allProjectsResponse?.data?.hasContent &&
        <div className={styles.emptyContainer}>
          <span>{t("projectAndMilestonePickerModalNoProjectText")}</span>
          <Button variant={ButtonVariants.filled}
                  heightVariant={ButtonHeight.short}
                  onClick={openNewProjectModal}>
            {t("projectAndMilestonePickerModalNoProjectButtonLabel")}
          </Button>
        </div>
      }

      {!isAllProjectsFetching && allProjectsResponse?.data?.hasContent &&
        <div className={styles.contentContainer}>
          {selectedProject &&
            <div className={styles.selectedProjectContainer}>
              <h3>{t("projectAndMilestonePickerModalSelectedProjectLabel")}</h3>
              <div className={styles.selectedProjectInfoContainer}>
                <LuBox className={"icon"} />
                <span className={"flex-1 line-clamp"}>{selectedProject.title}</span>
                <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={deselectProject}>
                  <LuX className={"icon"} />
                </Button>
              </div>
            </div>}

          {!selectedProject &&
            <div className={styles.projectListContainer}>
              <h3>{t("projectAndMilestonePickerModalSelectProjectLabel")}</h3>
              {allProjectsResponse?.data?.content?.map(project =>
                <Button key={`project-picker-project-${project.projectId}`}
                        className={styles.projectListProjectButton}
                        onClick={() => {
                          setSelectedProject(project);
                        }}>
                  <LuBox className={"icon"} />
                  <span className={"line-clamp"}>{project.title}</span>
                </Button>
              )}
            </div>
          }

          {selectedProject &&
            <div className={styles.milestonePickContainer}>
              <Line />
              <div className={"spacer-h-2"} />
              <div className={styles.contentContainer}>
                {selectedMilestone &&
                  <div className={styles.selectedMilestoneContainer}>
                    <h3>{t("projectAndMilestonePickerModalSelectedMilestoneLabel")}</h3>
                    <div className={styles.selectedMilestoneInfoContainer}>
                      <LuDiamond className={"icon"} />
                      <span className={"flex-1 line-clamp"}>{selectedMilestone.title}</span>
                      <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short}
                              onClick={deselectMilestone}>
                        <LuX className={"icon"} />
                      </Button>
                    </div>
                  </div>}

                {!selectedMilestone &&
                  <div className={styles.milestoneListContainer}>
                    <h3>{t("projectAndMilestonePickerModalSelectMilestoneLabel")}</h3>

                    {selectedProject?.milestones?.length == 0 &&
                      <div className={styles.emptyContainer}>
                        <div className={"spacer-h-2"} />
                        <span>{t("projectAndMilestonePickerModalNoMilestoneText")}</span>
                        <Button variant={ButtonVariants.filled}
                                heightVariant={ButtonHeight.short}
                                onClick={openNewMilestoneModal}>
                          {t("projectAndMilestonePickerModalNoMilestoneButtonLabel")}
                        </Button>
                      </div>
                    }

                    {selectedProject?.milestones?.map(milestone =>
                      <Button key={`project-picker-milestone-${milestone.milestoneId}`}
                              className={styles.milestoneListProjectButton}
                              onClick={() => {
                                selectMilestone(milestone);
                              }}>
                        <LuDiamond className={"icon"} />
                        <span className={"line-clamp"}>{milestone.title}</span>
                      </Button>
                    )}

                    {selectedProject && !selectedMilestone && selectedProject.milestones?.length != 0 &&
                      <Button variant={ButtonVariants.filled}
                              heightVariant={ButtonHeight.short}
                              onClick={openNewMilestoneModal}>
                        {t("projectAndMilestonePickerModalNoMilestoneButtonLabel")}
                      </Button>
                    }
                  </div>}

              </div>
            </div>
          }

          {selectedProject && selectedMilestone && onUnpick && <div className={styles.unpickButtonContainer}>
            <Button variant={ButtonVariants.filled}
                    heightVariant={ButtonHeight.short}
                    onClick={unpickClick}>
              {t("projectAndMilestonePickerModalUnpickButtonLabel")}
            </Button>
          </div>}
        </div>
      }
    </Modal>
  );
};

export default ProjectAndMilestonePickerModal;