import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { popTeamMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useSelectedOwners, useSetSelectedOwners, useTeam } from "../context/TaskListFilterBarContext";
import styles from "./OwnerFilterButton.module.css";

interface OwnerFilterButtonProps {}

const OwnerFilterButton: React.FC<OwnerFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();
  const selectedOwners = useSelectedOwners();
  const setSelectedOwners = useSetSelectedOwners();
  const isEmpty = selectedOwners?.length == 0;

  const onPick = (pickedList: TeamMemberDto[]) => {
    setSelectedOwners?.(pickedList);
  };

  const popPicker = () => {
    dispatch(
      popTeamMemberPickerModal({
        visible: true,
        teamId: team?.teamId,
        multiple: true,
        initialSelectionOnMultiple: selectedOwners,
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
        t("taskFilterOwnerFilterButtonEmpty")
      ) : (
        <b>
          {selectedOwners?.length == 1 ? (
            <div className={styles.singleItem}>
              {t("taskFilterOwnerFilterButtonSingleSelection")}
              <ProfilePhoto
                boringAvatarKey={selectedOwners[0]?.account.accountId || ""}
                storagePath={selectedOwners[0]?.account.profilePicture?.storagePath}
                wrapperClassName={styles.profilePic}
              />
              {selectedOwners[0]?.account.username}
            </div>
          ) : (
            t("taskFilterOwnerFilterButtonSelected")?.replace("${count}", `${selectedOwners?.length}`)
          )}
        </b>
      )}
    </Button>
  );
};

export default OwnerFilterButton;
