import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { popTeamMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useSelectedAssignees, useSetSelectedAssignees, useTeam } from "../context/TaskListFilterBarContext";
import styles from "./AssigneeFilterButton.module.css";

interface AssigneeFilterButtonProps {}

const AssigneeFilterButton: React.FC<AssigneeFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();
  const selectedAssignees = useSelectedAssignees();
  const setSelectedAssignees = useSetSelectedAssignees();
  const isEmpty = selectedAssignees?.length == 0;

  const onPick = (pickedList: TeamMemberDto[]) => {
    setSelectedAssignees?.(pickedList);
  };

  const popPicker = () => {
    dispatch(
      popTeamMemberPickerModal({
        visible: true,
        teamId: team?.teamId,
        multiple: true,
        initialSelectionOnMultiple: selectedAssignees,
        onPick,
      })
    );
  };

  return (
    <Button
      heightVariant={ButtonHeight.short}
      variant={isEmpty ? ButtonVariants.filled : ButtonVariants.filled2}
      onClick={popPicker}
    >
      {isEmpty ? (
        t("taskFilterAssigneeFilterButtonEmpty")
      ) : (
        <b>
          {selectedAssignees?.length == 1 ? (
            <div className={styles.singleItem}>
              {t("taskFilterAssigneeFilterButtonSingleSelection")}
              <ProfilePhoto
                boringAvatarKey={selectedAssignees[0]?.account.accountId || ""}
                storagePath={selectedAssignees[0]?.account.profilePicture?.storagePath}
                wrapperClassName={styles.profilePic}
              />
              {selectedAssignees[0]?.account.username}
            </div>
          ) : (
            t("taskFilterAssigneeFilterButtonSelected")?.replace("${count}", `${selectedAssignees?.length}`)
          )}
        </b>
      )}
    </Button>
  );
};

export default AssigneeFilterButton;
