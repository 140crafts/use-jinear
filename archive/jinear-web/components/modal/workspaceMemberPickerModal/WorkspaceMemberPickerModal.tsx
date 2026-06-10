import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useRetrieveWorkspaceMembersQuery } from "@/store/api/workspaceMemberApi";
import {
  closeWorkspaceMemberPickerModal, selectWorkspaceMemberPickerModalDeselectable,
  selectWorkspaceMemberPickerModalInitialSelectionOnMultiple,
  selectWorkspaceMemberPickerModalMultiple, selectWorkspaceMemberPickerModalOnDeselect,
  selectWorkspaceMemberPickerModalOnPick,
  selectWorkspaceMemberPickerModalVisible,
  selectWorkspaceMemberPickerModalWorkspaceId
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import { IoClose } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./WorkspaceMemberPickerModal.module.css";

interface WorkspaceMemberPickerModalProps {
}

const WorkspaceMemberPickerModal: React.FC<WorkspaceMemberPickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectWorkspaceMemberPickerModalVisible);
  const multiple = useTypedSelector(selectWorkspaceMemberPickerModalMultiple);
  const workspaceId = useTypedSelector(selectWorkspaceMemberPickerModalWorkspaceId);
  const initialSelectionOnMultiple = useTypedSelector(selectWorkspaceMemberPickerModalInitialSelectionOnMultiple);
  const onPick = useTypedSelector(selectWorkspaceMemberPickerModalOnPick);
  const deselectable = useTypedSelector(selectWorkspaceMemberPickerModalDeselectable);
  const onDeselect = useTypedSelector(selectWorkspaceMemberPickerModalOnDeselect);

  const inputRef = useRef<HTMLInputElement>(null);
  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const [selectedWorkspaceMembers, setSelectedWorkspaceMembers] = useState<WorkspaceMemberDto[]>([]);

  const { data: workspaceMemberListResponse, isFetching } = useRetrieveWorkspaceMembersQuery(
    { workspaceId: workspaceId || "" },
    {
      skip: workspaceId == null
    }
  );

  const filteredList =
    workspaceMemberListResponse?.data.content.filter(
      (workpsaceMemberDto) => searchValue == "" || workpsaceMemberDto.account.username?.indexOf(searchValue.toLowerCase()) != -1
    ) || [];

  useEffect(() => {
    setTimeout(() => {
      if (visible && inputRef.current) {
        inputRef.current.focus?.();
      }
    }, 250);
  }, [visible]);

  useEffect(() => {
    if (initialSelectionOnMultiple != null && initialSelectionOnMultiple?.length != 0) {
      setSelectedWorkspaceMembers(initialSelectionOnMultiple);
    }
  }, [initialSelectionOnMultiple]);

  useDebouncedEffect(() => setSearchValue(input), [input], 500);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const close = () => {
    setSearchValue("");
    setInput("");
    setSelectedWorkspaceMembers([]);
    dispatch(closeWorkspaceMemberPickerModal());
  };

  const addToSelected = (workspaceMemberDto: WorkspaceMemberDto) => {
    if (selectedWorkspaceMembers.filter((w) => w.workspaceMemberId == workspaceMemberDto.workspaceMemberId).length == 0) {
      setSelectedWorkspaceMembers([...selectedWorkspaceMembers, workspaceMemberDto]);
    }
  };

  const removeFromSelected = (selectedWorkspaceMemberDto: WorkspaceMemberDto) => {
    const filtered = selectedWorkspaceMembers.filter(
      (workspaceMemberDto) => workspaceMemberDto.workspaceMemberId != selectedWorkspaceMemberDto.workspaceMemberId
    );
    setSelectedWorkspaceMembers(filtered);
  };

  const pickAndClose = (selectedWorkspaceMemberDto: WorkspaceMemberDto) => {
    onPick?.([selectedWorkspaceMemberDto]);
    close?.();
  };

  const submitPickedAndClose = () => {
    onPick?.(selectedWorkspaceMembers);
    close?.();
  };

  const deselectAndClose = () => {
    onDeselect?.();
    close?.();
  };

  return (
    <Modal
      visible={visible}
      title={t("workspaceMemberPickerModalTitle")}
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
          placeholder={t("workspaceMemberPickerModalFilterPlaceholder")}
          value={input}
          onChange={onTextChange}
        />
      </div>

      <div className={styles.list}>
        {!isFetching && (
          <>
            {filteredList.map((workspaceMemberDto) => (
              <Button
                key={`workspace-member-search-result-list-${workspaceMemberDto.workspaceMemberId}`}
                variant={ButtonVariants.default}
                className={styles.listItemButton}
                onClick={() => {
                  multiple ? addToSelected(workspaceMemberDto) : pickAndClose(workspaceMemberDto);
                }}
              >
                <ProfilePhoto
                  boringAvatarKey={workspaceMemberDto.account.accountId || ""}
                  url={workspaceMemberDto.account.profilePicture?.url}
                  wrapperClassName={styles.profilePic}
                />
                {workspaceMemberDto.account.username}
              </Button>
            ))}
          </>
        )}

        <div className={styles.messageContainer}>
          {!isFetching && filteredList?.length == 0 && <div>{t("workspaceMemberPickerModalEmptyState")}</div>}
          {isFetching && <CircularProgress size={17} />}
        </div>
      </div>

      {multiple && selectedWorkspaceMembers.length != 0 && (
        <div className={styles.selectedTopicListContainer}>
          {selectedWorkspaceMembers.map((workspaceMember) => (
            <div key={`selected-workspace-member-${workspaceMember.workspaceMemberId}`}
                 className={styles.selectedTopicContainer}>
              <div className={styles.selectedTopicName}>
                <ProfilePhoto
                  boringAvatarKey={workspaceMember.account.accountId || ""}
                  url={workspaceMember.account.profilePicture?.url}
                  wrapperClassName={styles.profilePic}
                />
                {workspaceMember.account.username}
              </div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedTopicUnselectButton}
                onClick={() => removeFromSelected(workspaceMember)}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>
      )}
      {(multiple || deselectable) && (
        <div className={styles.actionBar}>
          <Button heightVariant={ButtonHeight.short} onClick={close}>
            {t("workspaceMemberPickerModalCancelButton")}
          </Button>
          <Button heightVariant={ButtonHeight.short} onClick={deselectAndClose}>
            {t("workspaceMemberPickerModalDeselectButton")}
          </Button>
          {multiple && <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            className={styles.contButton}
            onClick={submitPickedAndClose}
            disabled={selectedWorkspaceMembers?.length == 0 && !multiple}
          >
            {t("workspaceMemberPickerModalSelectButton")}
          </Button>}
        </div>
      )}
    </Modal>
  );
};

export default WorkspaceMemberPickerModal;
