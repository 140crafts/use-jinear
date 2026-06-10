import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import {
  closeTeamWorkflowStatusPickerModal,
  selectTeamWorkflowStatusPickerModalInitialSelectionOnMultiple,
  selectTeamWorkflowStatusPickerModalMultiple,
  selectTeamWorkflowStatusPickerModalOnPick,
  selectTeamWorkflowStatusPickerModalTeamId,
  selectTeamWorkflowStatusPickerModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IoCheckmarkCircle, IoCloseCircle, IoContrast, IoEllipseOutline, IoPauseCircleOutline } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./TeamWorkflowStatusModal.module.css";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { CircularProgress } from "@mui/material";
import { IoClose } from "react-icons/io5";

interface TeamWorkflowStatusModalProps {}

const ICON_SIZE = 15;
const groupIconMap = {
  BACKLOG: <IoPauseCircleOutline size={ICON_SIZE} />,
  NOT_STARTED: <IoEllipseOutline size={ICON_SIZE} />,
  STARTED: <IoContrast size={ICON_SIZE} />,
  COMPLETED: <IoCheckmarkCircle size={ICON_SIZE} />,
  CANCELLED: <IoCloseCircle size={ICON_SIZE} />,
};

const TeamWorkflowStatusModal: React.FC<TeamWorkflowStatusModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTeamWorkflowStatusPickerModalVisible);
  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const [selectedTeamWorkflowStatuses, setSelectedTeamWorkflowStatuses] = useState<TeamWorkflowStatusDto[]>([]);
  const teamId = useTypedSelector(selectTeamWorkflowStatusPickerModalTeamId);
  const multiple = useTypedSelector(selectTeamWorkflowStatusPickerModalMultiple);
  const initialSelectionOnMultiple = useTypedSelector(selectTeamWorkflowStatusPickerModalInitialSelectionOnMultiple);
  const onPick = useTypedSelector(selectTeamWorkflowStatusPickerModalOnPick);

  const { data: teamWorkflowStatusListResponse, isFetching } = useRetrieveAllFromTeamQuery(
    { teamId: teamId || "" },
    {
      skip: teamId == null,
    }
  );

  const filteredListBacklog =
    teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.BACKLOG?.filter(
      (teamWorkflowStatusDto) =>
        searchValue == "" || teamWorkflowStatusDto.name?.toLowerCase()?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  const filteredListNotStarted =
    teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.filter(
      (teamWorkflowStatusDto) =>
        searchValue == "" || teamWorkflowStatusDto.name?.toLowerCase()?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  const filteredListStarted =
    teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.STARTED?.filter(
      (teamWorkflowStatusDto) =>
        searchValue == "" || teamWorkflowStatusDto.name?.toLowerCase()?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  const filteredListCompleted =
    teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.COMPLETED?.filter(
      (teamWorkflowStatusDto) =>
        searchValue == "" || teamWorkflowStatusDto.name?.toLowerCase()?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  const filteredListCancelled =
    teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses.CANCELLED?.filter(
      (teamWorkflowStatusDto) =>
        searchValue == "" || teamWorkflowStatusDto.name?.toLowerCase()?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  const filteredListAll = [
    ...filteredListBacklog,
    ...filteredListNotStarted,
    ...filteredListStarted,
    ...filteredListCompleted,
    ...filteredListCancelled,
  ];

  useEffect(() => {
    if (initialSelectionOnMultiple != null && initialSelectionOnMultiple?.length != 0) {
      setSelectedTeamWorkflowStatuses(initialSelectionOnMultiple);
    }
  }, [initialSelectionOnMultiple]);

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const close = () => {
    setSearchValue("");
    setInput("");
    setSelectedTeamWorkflowStatuses([]);
    dispatch(closeTeamWorkflowStatusPickerModal());
  };

  const addToSelected = (selectedTeamWorkflowStatus: TeamWorkflowStatusDto) => {
    if (
      selectedTeamWorkflowStatuses.filter((t) => t.teamWorkflowStatusId == selectedTeamWorkflowStatus.teamWorkflowStatusId)
        .length == 0
    ) {
      setSelectedTeamWorkflowStatuses([...selectedTeamWorkflowStatuses, selectedTeamWorkflowStatus]);
    }
  };

  const removeFromSelected = (selectedTeamWorkflowStatus: TeamWorkflowStatusDto) => {
    const filtered = selectedTeamWorkflowStatuses.filter(
      (t) => t.teamWorkflowStatusId != selectedTeamWorkflowStatus.teamWorkflowStatusId
    );
    setSelectedTeamWorkflowStatuses(filtered);
  };

  const pickAndClose = (selectedTeamWorkflowStatus: TeamWorkflowStatusDto) => {
    onPick?.([selectedTeamWorkflowStatus]);
    close?.();
  };

  const submitPickedAndClose = () => {
    onPick?.(selectedTeamWorkflowStatuses);
    close?.();
  };

  return (
    <Modal
      visible={visible}
      title={t("teamWorkflowStatusPickerModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
      height={"height-medium-or-full"}
    >
      <div className={styles.content}>
        <input
          ref={inputRef}
          type={"text"}
          className={styles.searchInput}
          placeholder={t("teamWorkflowStatusPickerModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>
      <div className={styles.list}>
        {!isFetching && (
          <>
            {filteredListAll.map((teamWorkflowStatusDto) => (
              <Button
                key={`team-workflow-status-search-result-list-${teamWorkflowStatusDto.teamWorkflowStatusId}`}
                variant={ButtonVariants.default}
                className={styles.listItemButton}
                onClick={() => {
                  multiple ? addToSelected(teamWorkflowStatusDto) : pickAndClose(teamWorkflowStatusDto);
                }}
              >
                {groupIconMap?.[teamWorkflowStatusDto.workflowStateGroup]}
                {teamWorkflowStatusDto.name}
              </Button>
            ))}
          </>
        )}

        <div className={styles.messageContainer}>
          {!isFetching && filteredListAll?.length == 0 && <div>{t("teamWorkflowStatusPickerModalEmptyState")}</div>}
          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>

      {multiple && selectedTeamWorkflowStatuses.length != 0 && (
        <div className={styles.selectedItemListContainer}>
          {selectedTeamWorkflowStatuses.map((teamWorkflowStatusDto) => (
            <div
              key={`selected-team-workflow-status-${teamWorkflowStatusDto.teamWorkflowStatusId}`}
              className={styles.selectedItemContainer}
            >
              <div className={styles.selectedItemName}>
                {groupIconMap?.[teamWorkflowStatusDto.workflowStateGroup]}
                {teamWorkflowStatusDto.name}
              </div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedItemUnselectButton}
                onClick={() => removeFromSelected(teamWorkflowStatusDto)}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>
      )}
      {multiple && (
        <div className={styles.actionBar}>
          <Button heightVariant={ButtonHeight.short} onClick={close}>
            {t("teamWorkflowStatusPickerModalCancelButton")}
          </Button>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            className={styles.contButton}
            onClick={submitPickedAndClose}
            disabled={selectedTeamWorkflowStatuses?.length == 0 && !multiple}
          >
            {t("teamWorkflowStatusPickerModalSelectButton")}
          </Button>
        </div>
      )}
    </Modal>
  );
};

export default TeamWorkflowStatusModal;
