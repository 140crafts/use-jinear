import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { TaskBoardDto, TaskInitializeRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popBoardPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import { IoReaderOutline } from "react-icons/io5";
import styles from "./BoardPickerButton.module.css";
import {
  ITeamMemberPickerButtonRef
} from "@/components/form/newTaskForm/teamMemberPickerButton/TeamMemberPickerButton";

interface BoardPickerButtonProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  initialBoard?: TaskBoardDto;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
}

export interface IBoardPickerButtonRef {
  reset: () => void;
}

const logger = Logger("BoardPickerButton");
const BoardPickerButton = ({ workspace, team, initialBoard, register, setValue }: BoardPickerButtonProps, ref: any) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedBoard, setSelectedBoard] = useState<TaskBoardDto>();

  logger.log({ initialBoard, selectedBoard });

  useEffect(() => {
    setValue("boardId", selectedBoard ? selectedBoard.taskBoardId : "no-board");
  }, [selectedBoard]);

  useEffect(() => {
    logger.log({ effect: initialBoard });
    if (initialBoard) {
      setTimeout(() => {
        setSelectedBoard(initialBoard);
      }, 500);
    }
  }, [initialBoard]);

  useImperativeHandle(ref, () => ({
    reset: () => setSelectedBoard(undefined)
  }));

  const onModalPick = (selection: TaskBoardDto[]) => {
    logger.log({ boardSelection: selection });
    if (selection.length != 0) {
      setSelectedBoard(selection[0]);
    }
  };

  const popTopicPicker = () => {
    dispatch(
      popBoardPickerModal({
        visible: true,
        multiple: false,
        teamId: team.teamId,
        workspaceId: workspace.workspaceId,
        onPick: onModalPick
      })
    );
  };

  const deselect = () => {
    setSelectedBoard(undefined);
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={selectedBoard != null}
        onPickClick={popTopicPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <IoReaderOutline />
            {selectedBoard?.title}
          </div>
        }
        emptySelectionLabel={t("newTaskFormPickBoardButtonLabel")}
        onUnpickClick={deselect}
      />
      <input type="hidden" value={selectedBoard?.taskBoardId || "no-board"} {...register("boardId")} />
    </div>
  );
};

export default forwardRef<IBoardPickerButtonRef, BoardPickerButtonProps>(BoardPickerButton);