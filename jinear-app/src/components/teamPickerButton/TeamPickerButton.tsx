import React, {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import styles from "./TeamPickerButton.module.css";
import useTranslation from "@/locals/useTranslation";
import {useAppDispatch} from "@/store";
import {useRetrieveWorkspaceTeamsQuery} from "@/api/teamApi";
import type {TeamDto} from "@/be/jinear-core";
import {popTeamPickerModalV2} from "@/slice/modalSlice";
import Logger from "@/util/logger";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import {LuUsers} from "react-icons/lu";

export interface ITeamPickerButtonRef {
    popPicker: () => void;
}

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


const TeamPickerButton = ({
                              workspaceId,
                              initialSelectedTeams = [],
                              multiple,
                              onPick,
                              useJoinedNameOnMultiplePick,
                              label,
                              withoutUnpickButton = false,
                              selectedLabel
                          }: TeamPickerButtonProps, ref: any) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [currentPick, setCurrentPick] = useState<TeamDto[]>([]);
    const joinedName = currentPick?.map(el => el.tag).join(", ");

    useImperativeHandle(ref, () => ({
        popPicker
    }));

    useEffect(() => {
        setCurrentPick(initialSelectedTeams);
    }, [JSON.stringify(initialSelectedTeams)]);

    const {
        data: teamsResponse,
        isLoading,
        isFetching
    } = useRetrieveWorkspaceTeamsQuery(workspaceId || "", {skip: workspaceId == null || workspaceId == ""});

    const onPickerPicked = (pickedList: TeamDto[]) => {
        logger.log({teamPickerButtonOnPickedList: pickedList});
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
                        <LuUsers className={styles.icon}/>
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
export default forwardRef<ITeamPickerButtonRef, TeamPickerButtonProps>(TeamPickerButton);
// export default TeamPickerButton;