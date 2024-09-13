import React, { useMemo } from "react";
import styles from "./TeamPickerModalV2.module.css";
import GenericSelectModal, {
  IGenericPickerModalElement
} from "@/components/modal/genericSelectModal/GenericSelectModal";
import {
  closeTeamPickerModalV2,
  selectTeamPickerModalV2InitialSelectionOnMultiple,
  selectTeamPickerModalV2ModalData,
  selectTeamPickerModalV2Multiple, selectTeamPickerModalV2OnPick, selectTeamPickerModalV2Visible,
  selectTeamPickerModalV2WorkspaceId
} from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "@/locals/useTranslation";
import { TeamDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";

interface TeamPickerModalV2Props {

}

const convertTeamDtoArrToGenericPickerModalElement = (teamDtoList?: TeamDto[]): IGenericPickerModalElement[] => {
  return teamDtoList?.map(teamDto => {
    return {
      id: teamDto.teamId,
      label: teamDto.name,
      data: teamDto
    };
  }) || [];
};

const logger = Logger("TeamPickerModalV2");

const TeamPickerModalV2: React.FC<TeamPickerModalV2Props> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTeamPickerModalV2Visible);
  const workspaceId = useTypedSelector(selectTeamPickerModalV2WorkspaceId);
  const multiple = useTypedSelector(selectTeamPickerModalV2Multiple);
  const modalData = useTypedSelector(selectTeamPickerModalV2ModalData);
  const initialSelectionOnMultiple = useTypedSelector(selectTeamPickerModalV2InitialSelectionOnMultiple);
  const onPick = useTypedSelector(selectTeamPickerModalV2OnPick);

  const genericPickerTeamDtoModalData = convertTeamDtoArrToGenericPickerModalElement(modalData);
  const genericPickerTeamDtoInitialData = convertTeamDtoArrToGenericPickerModalElement(initialSelectionOnMultiple);

  const onGenericModalPick = (pickedList: IGenericPickerModalElement[]) => {
    logger.log({ onGenericModalPick: pickedList });
    onPick?.(pickedList?.map(el => el.data));
  };

  const close = () => {
    dispatch(closeTeamPickerModalV2());
  };

  return (
    <GenericSelectModal
      visible={visible}
      title={t("teamPickerModalV2Title")}
      multiple={multiple}
      modalData={genericPickerTeamDtoModalData}
      initialSelectionOnMultiple={genericPickerTeamDtoInitialData}
      onPick={onGenericModalPick}
      searchable={true}
      searchInputPlaceholder={t("teamPickerModalV2SearchBarPlaceholder")}
      emptyLabel={t("teamPickerModalV2NoTeamFound")}
      // belowSearchButtonLabel
      // onBelowSearchButtonClick
      // emptyButtonLabel
      // onEmptyButtonClick
      requestClose={close}
    />
  );
};

export default TeamPickerModalV2;