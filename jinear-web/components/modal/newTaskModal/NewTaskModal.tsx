import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeNewTaskModal, popNewCalendarIntegrationModal,
  selectNewTaskModalInitialAssignedDate,
  selectNewTaskModalInitialAssignedDateIsPrecise,
  selectNewTaskModalInitialDueDate,
  selectNewTaskModalInitialDueDateIsPrecise,
  selectNewTaskModalInitialRelatedFeedItemData,
  selectNewTaskModalSubTaskOf,
  selectNewTaskModalSubTaskOfLabel,
  selectNewTaskModalTeam,
  selectNewTaskModalVisible,
  selectNewTaskModalWorkspace
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import Modal from "../modal/Modal";
import styles from "./NewTaskModal.module.scss";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import strings from "@/locals/strings";
import { useHasExternalCalendars } from "@/hooks/calendar/useHasExternalCalendars";
import ExternalCalendarAndSourcePicker
  from "@/components/externalCalendarAndSourcePicker/ExternalCalendarAndSourcePicker";
import NewCalendarEventForm from "@/components/form/newCalendarEventForm/NewCalendarEventForm";
import Logger from "@/utils/logger";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuCalendarPlus } from "react-icons/lu";

interface NewTaskModalProps {
}

type IInputType = "TASK" | "EVENT";
const INPUT_TYPES: IInputType[] = ["TASK", "EVENT"];

const logger = Logger("NewTaskModal");

const NewTaskModal: React.FC<NewTaskModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewTaskModalVisible);
  const subTaskOf = useTypedSelector(selectNewTaskModalSubTaskOf);
  const subTaskOfLabel = useTypedSelector(selectNewTaskModalSubTaskOfLabel);
  const workspace = useTypedSelector(selectNewTaskModalWorkspace);
  const team = useTypedSelector(selectNewTaskModalTeam);
  const initialAssignedDate = useTypedSelector(selectNewTaskModalInitialAssignedDate);
  const initialAssignedDateIsPrecise = useTypedSelector(selectNewTaskModalInitialAssignedDateIsPrecise);
  const initialDueDate = useTypedSelector(selectNewTaskModalInitialDueDate);
  const initialDueDateIsPrecise = useTypedSelector(selectNewTaskModalInitialDueDateIsPrecise);
  const initialRelatedFeedItemData = useTypedSelector(selectNewTaskModalInitialRelatedFeedItemData);
  const workspaceId = workspace?.workspaceId;

  const [newInputType, setNewInputType] = useState<IInputType>(INPUT_TYPES[0]);
  const hasExternalCalendarMembership = useHasExternalCalendars(workspaceId);
  logger.log({ hasExternalCalendarMembership });
  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeNewTaskModal());
  };

  const changeViewType = (value: string, index: number) => {
    if (value && (value == "TASK" || value == "EVENT")) {
      setNewInputType(value);
    }
  };

  const popCalendarIntegrationModal = () => {
    dispatch(popNewCalendarIntegrationModal({ workspaceId, visible: true }));
  };

  return (
    <Modal
      visible={visible}
      // title={t("newTaskModalTitle")}
      bodyClass={styles.container}
      // width={isMobile ? "fullscreen" : "large"}
      width={"large"}
      containerClassName={styles.modalContainer}
    >

      {!(subTaskOf != null || initialRelatedFeedItemData != null) &&
        <div className={styles.segmentedControlContainer}>
          <SegmentedControl
            id="new-task-modal-new-type-segment-control"
            name="new-task-modal-new-type-segment-control"
            defaultIndex={INPUT_TYPES.indexOf(newInputType)}
            segments={[
              { label: t("newTaskEventType_TASK"), value: "TASK" },
              { label: t("newTaskEventType_EVENT"), value: "EVENT" }
            ]}
            segmentLabelClassName={styles.viewTypeSegmentLabel}
            callback={changeViewType}
          />
        </div>}

      {workspaceId && team?.teamId && newInputType == "TASK" && (
        <NewTaskForm
          workspace={workspace}
          initialTeam={team}
          subTaskOf={subTaskOf}
          subTaskOfLabel={subTaskOfLabel}
          initialAssignedDate={initialAssignedDate}
          initialAssignedDateIsPrecise={initialAssignedDateIsPrecise}
          initialDueDate={initialDueDate}
          initialDueDateIsPrecise={initialDueDateIsPrecise}
          initialRelatedFeedItemData={initialRelatedFeedItemData}
          onClose={close}
        />
      )}
      {workspaceId && newInputType == "EVENT" && (
        hasExternalCalendarMembership ?
          <NewCalendarEventForm
            workspaceId={workspaceId}
            onClose={close}
          /> :
          <div className={styles.addNewCalendarContainer}>
          <span className={styles.addNewCalendarText}>
            {t("newTaskModalAddCalendar")}
          </span>
            <div className={styles.addNewCalendarActionBar}>
              <Button
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.default}
                onClick={close}
              >
                {t("newTaskModalCalendarEventCancel")}
              </Button>

              <Button
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.contrast}
                onClick={popCalendarIntegrationModal}
                className={styles.addCalendarIntegrationButton}
              >
                <LuCalendarPlus />
                {t("sideMenuAddCalendarIntegrationLabel")}
              </Button>
            </div>
          </div>
      )}

    </Modal>
  );
};

export default NewTaskModal;
