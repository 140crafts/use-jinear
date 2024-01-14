import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { queryStateAnyToStringConverter, queryStateArrayParser, useQueryState, useSetQueryState } from "@/hooks/useQueryState";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { popTeamMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTeam } from "../context/TaskListFilterBarContext";
import styles from "./OwnerFilterButton.module.css";

interface OwnerFilterButtonProps {}

const OwnerFilterButton: React.FC<OwnerFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();

  const setQueryState = useSetQueryState();
  const selectedOwnerIds = useQueryState<string[]>("ownerIds", queryStateArrayParser);
  const isEmpty = selectedOwnerIds == null || selectedOwnerIds.length == 0;

  const { data: teamMemberListResponse } = useRetrieveTeamMembersQuery(
    { teamId: team?.teamId || "" },
    {
      skip: team == null,
    }
  );
  const selectedOwners = teamMemberListResponse?.data.content.filter(
    (member) => selectedOwnerIds && selectedOwnerIds.indexOf(member.accountId) != -1
  );

  const onPick = (pickedList: TeamMemberDto[]) => {
    const pickedIds = pickedList.map((teamMemberDto) => teamMemberDto.accountId);
    setQueryState("ownerIds", queryStateAnyToStringConverter(pickedIds));
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
