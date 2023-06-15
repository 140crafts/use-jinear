import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { TeamMemberDto } from "@/model/be/jinear-core";
import { useRetrieveTeamMembersQuery } from "@/store/api/teamMemberApi";
import {
  closeTeamMemberPickerModal,
  selectTeamMemberPickerModalMultiple,
  selectTeamMemberPickerModalOnPick,
  selectTeamMemberPickerModalTeamId,
  selectTeamMemberPickerModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useRef, useState } from "react";
import { IoClose } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./TeamMemberPickerModal.module.css";

interface TeamMemberPickerModalProps {}

const TeamMemberPickerModal: React.FC<TeamMemberPickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectTeamMemberPickerModalVisible);
  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const [selectedTeamMembers, setSelectedTeamMembers] = useState<TeamMemberDto[]>([]);
  const teamId = useTypedSelector(selectTeamMemberPickerModalTeamId);
  const multiple = useTypedSelector(selectTeamMemberPickerModalMultiple);
  const onPick = useTypedSelector(selectTeamMemberPickerModalOnPick);

  const { data: teamMemberListResponse, isFetching } = useRetrieveTeamMembersQuery(
    { teamId: teamId || "" },
    {
      skip: teamId == null,
    }
  );

  const filteredList =
    teamMemberListResponse?.data.content.filter(
      (teamMemberDto) => searchValue == "" || teamMemberDto.account.username?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const close = () => {
    setSearchValue("");
    setInput("");
    dispatch(closeTeamMemberPickerModal());
  };

  const addToSelected = (teamMemberDto: TeamMemberDto) => {
    if (selectedTeamMembers.filter((t) => t.teamMemberId == teamMemberDto.teamMemberId).length == 0) {
      setSelectedTeamMembers([...selectedTeamMembers, teamMemberDto]);
    }
  };

  const removeFromSelected = (selectedTeamMemberDto: TeamMemberDto) => {
    const filtered = selectedTeamMembers.filter(
      (teamMemberDto) => teamMemberDto.teamMemberId != selectedTeamMemberDto.teamMemberId
    );
    setSelectedTeamMembers(filtered);
  };

  const pickAndClose = (selectedTeamMemberDto: TeamMemberDto) => {
    onPick?.([selectedTeamMemberDto]);
    close?.();
  };

  const submitPickedAndClose = () => {
    onPick?.(selectedTeamMembers);
    close?.();
  };

  return (
    <Modal
      visible={visible}
      title={t("teamMemberPickerModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
      height={"height-medium-or-full"}
    >
      <div className={styles.content}>
        <input
          ref={inputRef}
          type={"text"}
          className={styles.searchInput}
          placeholder={t("teamMemberPickerModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>

      <div className={styles.list}>
        {!isFetching && (
          <>
            {filteredList.map((teamMemberDto) => (
              <Button
                key={`team-member-search-result-list-${teamMemberDto.teamMemberId}`}
                variant={ButtonVariants.default}
                className={styles.listItemButton}
                onClick={() => {
                  multiple ? addToSelected(teamMemberDto) : pickAndClose(teamMemberDto);
                }}
              >
                <ProfilePhoto
                  boringAvatarKey={teamMemberDto.account.accountId || ""}
                  storagePath={teamMemberDto.account.profilePicture?.storagePath}
                  wrapperClassName={styles.profilePic}
                />
                {teamMemberDto.account.username}
              </Button>
            ))}
          </>
        )}
        <div className={styles.messageContainer}>
          {!isFetching && filteredList?.length == 0 && <div>{t("teamMemberPickerModalEmptyState")}</div>}
          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>
      {multiple && selectedTeamMembers.length != 0 && (
        <div className={styles.selectedTopicListContainer}>
          {selectedTeamMembers.map((teamMember) => (
            <div key={`selected-team-member-${teamMember.teamMemberId}`} className={styles.selectedTopicContainer}>
              <div className={styles.selectedTopicName}>
                <ProfilePhoto
                  boringAvatarKey={teamMember.account.accountId || ""}
                  storagePath={teamMember.account.profilePicture?.storagePath}
                  wrapperClassName={styles.profilePic}
                />
                {teamMember.account.username}
              </div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedTopicUnselectButton}
                onClick={() => removeFromSelected(teamMember)}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>
      )}
      {multiple && (
        <div className={styles.actionBar}>
          <Button heightVariant={ButtonHeight.short} onClick={close}>
            {t("teamMemberPickerModalCancelButton")}
          </Button>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            className={styles.contButton}
            onClick={submitPickedAndClose}
            disabled={selectedTeamMembers?.length == 0}
          >
            {t("teamMemberPickerModalSelectButton")}
          </Button>
        </div>
      )}
    </Modal>
  );
};

export default TeamMemberPickerModal;
