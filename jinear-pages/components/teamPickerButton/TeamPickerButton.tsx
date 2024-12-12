import React, { useEffect, useState } from "react";
import styles from "./TeamPickerButton.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { useRetrieveWorkspaceTeamsQuery } from "@/api/teamApi";
import { TeamDto } from "@/be/jinear-core";
import { popTeamPickerModalV2 } from "@/slice/modalSlice";
import Logger from "@/utils/logger";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { LuUsers } from "react-icons/lu";

interface TeamPickerButtonProps {
  workspaceId?: string;
  initialSelectedTeams?: TeamDto[];
  multiple: boolean;
  onPick: (picked: TeamDto[]) => void;
  useJoinedNameOnMultiplePick?: boolean;
  label?: string;
  withoutUnpickButton?: boolean;
  selectedLabel?: string;
}

const logger = Logger("TeamPickerButton");

const TeamPickerButton: React.FC<TeamPickerButtonProps> = ({
                                                             workspaceId,
                                                             initialSelectedTeams = [],
                                                             multiple,
                                                             onPick,
                                                             useJoinedNameOnMultiplePick,
                                                             label,
                                                             withoutUnpickButton = false,
                                                             selectedLabel
                                                           }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [currentPick, setCurrentPick] = useState<TeamDto[]>([]);
  const joinedName = currentPick?.map(el => el.tag).join(", ");

  useEffect(() => {
    setCurrentPick(initialSelectedTeams);
  }, [JSON.stringify(initialSelectedTeams)]);

  const {
    data: teamsResponse,
    isLoading,
    isFetching
  } = useRetrieveWorkspaceTeamsQuery(workspaceId || "", { skip: workspaceId == null || workspaceId == "" });

  const onPickerPicked = (pickedList: TeamDto[]) => {
    logger.log({ teamPickerButtonOnPickedList: pickedList });
    setCurrentPick(pickedList);
    onPick?.(pickedList);
  };

  const deselect = () => {
    setCurrentPick([]);
    onPick?.([]);
  };

  const popPicker = () => {
    if (teamsResponse && teamsResponse.data) {
      dispatch(popTeamPickerModalV2({
        visible: true,
        workspaceId,
        multiple,
        modalData: teamsResponse.data?.filter(team => team.teamState == "ACTIVE") || [],
        initialSelectionOnMultiple: currentPick,
        onPick: onPickerPicked
      }));
    }
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={currentPick?.length != 0}
        onPickClick={popPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            {selectedLabel && <span>{selectedLabel}</span>}
            <LuUsers className={styles.icon} />
            <span>
              {
                currentPick?.length == 1 ?
                  currentPick?.[0]?.name :
                  useJoinedNameOnMultiplePick ?
                    joinedName :
                    t("teamPickerButtonMultiplePickedLabel").replace("${number}", `${currentPick?.length}`)
              }
          </span>
          </div>
        }
        emptySelectionLabel={label ?? t("teamPickerButtonLabel")}
        onUnpickClick={deselect}
        disabled={isLoading || isFetching}
        withoutUnpickButton={withoutUnpickButton}
      />
    </div>
  );
};

export default TeamPickerButton;