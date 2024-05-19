import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import styles from "./WorkspaceMemberInputPicker.module.css";
import { useRetrieveWorkspaceMembersQuery } from "@/api/workspaceMemberApi";
import { WorkspaceMemberDto } from "@/be/jinear-core";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import Button, { ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { CircularProgress } from "@mui/material";
import useTranslation from "@/locals/useTranslation";
import { IoClose, IoRadioButtonOff, IoRadioButtonOn } from "react-icons/io5";
import Logger from "@/utils/logger";
import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";

interface WorkspaceMemberInputPickerProps {
  workspaceId: string;
  onSelectionChange: (selection: WorkspaceMemberDto[]) => void;
  disabled: boolean;
}

const logger = Logger("WorkspaceMemberInputPicker");

const WorkspaceMemberInputPicker: React.FC<WorkspaceMemberInputPickerProps> = ({
                                                                                 workspaceId,
                                                                                 onSelectionChange,
                                                                                 disabled = false
                                                                               }) => {
  const { t } = useTranslation();
  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const [selectedWorkspaceMembers, setSelectedWorkspaceMembers] = useState<WorkspaceMemberDto[]>([]);
  const { data: workspaceMemberListResponse, isFetching } = useRetrieveWorkspaceMembersQuery({ workspaceId });
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const workspaceOtherMembersList = workspaceMemberListResponse?.data.content.filter((workspaceMemberDto) => workspaceMemberDto.accountId != currentAccountId) || [];
  const filteredList = workspaceOtherMembersList.filter((workspaceMemberDto) => searchValue == "" || workspaceMemberDto.account.username?.indexOf(searchValue.toLowerCase()) != -1) || [];

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  useEffect(() => {
    onSelectionChange?.(selectedWorkspaceMembers);
  }, [onSelectionChange, selectedWorkspaceMembers]);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const toggleSelected = (workspaceMemberDto: WorkspaceMemberDto) => {
    const isSelected = selectedWorkspaceMembers?.map(wm => wm.accountId).indexOf(workspaceMemberDto.accountId) != -1;
    isSelected ? removeFromSelected(workspaceMemberDto.accountId) : addToSelected(workspaceMemberDto);
  };

  const removeFromSelected = (accountId: string) => {
    setSelectedWorkspaceMembers(selectedWorkspaceMembers => selectedWorkspaceMembers.filter(wm => wm.accountId != accountId));
    resetInput();
    inputRef.current?.focus();
  };

  const addToSelected = (workspaceMemberDto: WorkspaceMemberDto) => {
    setSelectedWorkspaceMembers(selectedWorkspaceMembers => [...selectedWorkspaceMembers, workspaceMemberDto]);
    resetInput();
    inputRef.current?.focus();
  };

  const resetInput = () => {
    setInput("");
    setSearchValue("");
  };

  logger.log({ selectedWorkspaceMembers });
  return (
    <div className={styles.container}>
      <div className={styles.searchInputContainer}>

        <div className={styles.selectedMemberListContainer}>
          {selectedWorkspaceMembers.map((workspaceMember) => (
            <div key={`input-selected-workspace-member-${workspaceMember.workspaceMemberId}`}
                 className={styles.selectedMemberContainer}>
              <div className={styles.selectedMemberName}>
                <ProfilePhoto
                  boringAvatarKey={workspaceMember.account.accountId || ""}
                  storagePath={workspaceMember.account.profilePicture?.storagePath}
                  wrapperClassName={styles.profilePic}
                />
                {workspaceMember.account.username}
              </div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedTopicUnselectButton}
                onClick={() => removeFromSelected(workspaceMember.accountId)}
                disabled={disabled}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>

        <input
          disabled={disabled}
          ref={inputRef}
          type={"text"}
          className={styles.searchInput}
          placeholder={t("workspaceMemberPickerModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>

      <div className={styles.list}>
        {!isFetching && (
          <>
            {filteredList.map((workspaceMemberDto) => {
              const isSelected = selectedWorkspaceMembers?.map(wm => wm.accountId).indexOf(workspaceMemberDto.accountId) != -1;
              return (
                <Button
                  key={`workspace-member-search-input-result-list-${workspaceMemberDto.workspaceMemberId}`}
                  variant={isSelected ? ButtonVariants.filled2 : ButtonVariants.default}
                  className={styles.listItemButton}
                  onClick={() => toggleSelected(workspaceMemberDto)}
                  disabled={disabled}
                >
                  <ProfilePhoto
                    boringAvatarKey={workspaceMemberDto.account.accountId || ""}
                    storagePath={workspaceMemberDto.account.profilePicture?.storagePath}
                    wrapperClassName={styles.profilePic}
                  />
                  <div className={"single-line"}>
                    {workspaceMemberDto.account.username}
                  </div>
                  <div className={"flex-1"} />
                  {isSelected ?
                    <IoRadioButtonOn /> : <IoRadioButtonOff />}
                </Button>
              );
            })}
          </>
        )}

        <div className={styles.messageContainer}>
          {!isFetching && filteredList?.length == 0 && <div>{t("workspaceMemberInputPickerModalEmptyState")}</div>}
          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>

    </div>
  );
};

export default WorkspaceMemberInputPicker;