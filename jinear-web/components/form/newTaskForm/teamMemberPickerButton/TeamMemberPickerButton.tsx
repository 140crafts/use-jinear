import ProfilePhoto from "@/components/profilePhoto";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { TaskInitializeRequest, TeamMemberDto } from "@/model/be/jinear-core";
import { popTeamMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import styles from "./TeamMemberPickerButton.module.css";

interface TeamMemberPickerButtonProps {
  teamId: string;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
}

const logger = Logger("TeamMemberPickerButton");

const TeamMemberPickerButton: React.FC<TeamMemberPickerButtonProps> = ({ teamId, register, setValue }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedTeamMember, setSelectedTeamMember] = useState<TeamMemberDto>();

  useEffect(() => {
    logger.log({ selectedTeamMember });
    setValue("assignedTo", selectedTeamMember ? selectedTeamMember.accountId : "no-assignee");
  }, [selectedTeamMember]);

  const onModalPick = (selection: TeamMemberDto[]) => {
    if (selection.length != 0) {
      setSelectedTeamMember(selection[0]);
    }
  };

  const popTeamMemberPicker = () => {
    dispatch(popTeamMemberPickerModal({ visible: true, multiple: false, teamId, onPick: onModalPick }));
  };

  const deselect = () => {
    setSelectedTeamMember(undefined);
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={selectedTeamMember != null}
        onPickClick={popTeamMemberPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <ProfilePhoto
              boringAvatarKey={selectedTeamMember?.account.accountId || ""}
              storagePath={selectedTeamMember?.account.profilePicture?.storagePath}
              wrapperClassName={styles.profilePic}
            />
            {selectedTeamMember?.account.username}
          </div>
        }
        emptySelectionLabel={t("newTaskFormPickAssigneeButtonLabel")}
        onUnpickClick={deselect}
      />
      <input type="hidden" value={selectedTeamMember?.accountId || "no-assignee"} {...register("assignedTo")} />
    </div>
  );
};

export default TeamMemberPickerButton;
