import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import {
  queryStateAnyToStringConverter,
  queryStateArrayParser,
  useQueryState,
  useSetQueryState
} from "@/hooks/useQueryState";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import { popTeamMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useTeam } from "../context/TaskListFilterBarContext";
import styles from "./AssigneeFilterButton.module.css";

interface AssigneeFilterButtonProps {
}

const AssigneeFilterButton: React.FC<AssigneeFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const team = useTeam();

  const setQueryState = useSetQueryState();
  const selectedAssigneeIds = useQueryState<string[]>("assigneeIds", queryStateArrayParser);
  const isEmpty = selectedAssigneeIds == null || selectedAssigneeIds.length == 0;

  const { data: teamMemberListResponse } = useRetrieveTeamMembersQuery(
    { teamId: team?.teamId || "" },
    {
      skip: team == null
    }
  );
  const selectedAssignees = teamMemberListResponse?.data.content.filter(
    (member) => selectedAssigneeIds && selectedAssigneeIds.indexOf(member.accountId) != -1
  );

  const onPick = (pickedList: TeamMemberDto[]) => {
    const pickedIds = pickedList.map((teamMemberDto) => teamMemberDto.accountId);
    setQueryState("assigneeIds", queryStateAnyToStringConverter(pickedIds));
  };

  const popPicker = () => {
    dispatch(
      popTeamMemberPickerModal({
        visible: true,
        teamId: team?.teamId,
        multiple: true,
        initialSelectionOnMultiple: selectedAssignees,
        onPick
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
                url={selectedAssignees[0]?.account.profilePicture?.url}
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
