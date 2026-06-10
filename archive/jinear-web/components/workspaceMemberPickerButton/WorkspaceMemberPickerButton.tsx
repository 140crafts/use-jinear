import React, { useEffect, useState } from "react";
import styles from "./WorkspaceMemberPickerButton.module.css";
import { WorkspaceMemberDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { popWorkspaceMemberPickerModal } from "@/slice/modalSlice";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { LuUser } from "react-icons/lu";
import ProfilePhoto from "@/components/profilePhoto";

interface WorkspaceMemberPickerButtonProps {
  workspaceId?: string;
  initialSelectedMembers?: WorkspaceMemberDto[];
  multiple: boolean;
  onPick: (picked: WorkspaceMemberDto[]) => void;
  useJoinedNameOnMultiplePick?: boolean;
  label?: string;
  selectionLabel?: string;
  deselectableFromModal?: boolean;
  onDeselect?: () => void;
}

const logger = Logger("WorkspaceMemberPickerButton");

const WorkspaceMemberPickerButton: React.FC<WorkspaceMemberPickerButtonProps> = ({
                                                                                   workspaceId,
                                                                                   initialSelectedMembers = [],
                                                                                   multiple,
                                                                                   onPick,
                                                                                   useJoinedNameOnMultiplePick = false,
                                                                                   label,
                                                                                   selectionLabel,
                                                                                   deselectableFromModal,
                                                                                   onDeselect
                                                                                 }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [currentPick, setCurrentPick] = useState<WorkspaceMemberDto[]>([]);
  const joinedName = currentPick?.map(el => el.account?.username).join(", ");

  useEffect(() => {
    setCurrentPick(initialSelectedMembers);
  }, [JSON.stringify(initialSelectedMembers)]);

  const onPickerPicked = (pickedList: WorkspaceMemberDto[]) => {
    logger.log({ WorkspaceMemberPickerButton: pickedList });
    setCurrentPick(pickedList);
    onPick?.(pickedList);
  };

  const deselect = () => {
    setCurrentPick([]);
    onPick?.([]);
  };

  const popPicker = () => {
    dispatch(popWorkspaceMemberPickerModal({
      visible: true,
      workspaceId,
      multiple,
      initialSelectionOnMultiple: currentPick,
      onPick: onPickerPicked,
      deselectable: deselectableFromModal,
      onDeselect
    }));
  };


  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={currentPick?.length != 0}
        onPickClick={popPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            {selectionLabel && <span>{selectionLabel}</span>}
            {currentPick?.length == 1 ?
              <ProfilePhoto
                boringAvatarKey={currentPick?.[0].account.accountId || ""}
                url={currentPick?.[0].account.profilePicture?.url}
                wrapperClassName={styles.profilePic}
              /> :
              <LuUser className={styles.icon} />}
            <span>
              {
                currentPick?.length == 1 ?
                  currentPick?.[0]?.account.username :
                  useJoinedNameOnMultiplePick ?
                    joinedName :
                    t("workspaceMemberPickerButtonMultiplePickedLabel").replace("${number}", `${currentPick?.length}`)
              }
          </span>
          </div>
        }
        emptySelectionLabel={label ?? t("workspaceMemberPickerButtonLabel")}
        onUnpickClick={deselect}
        withoutUnpickButton={deselectableFromModal}
      />
    </div>
  );
};

export default WorkspaceMemberPickerButton;