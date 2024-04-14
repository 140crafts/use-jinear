import React from "react";
import styles from "./WorkspaceMembersTab.module.scss";
import { useAddChannelMemberMutation, useRetrieveChannelMembersQuery } from "@/api/channelMemberApi";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import MemberRow from "@/components/modal/channelSettingsModal/workspaceMembersTab/memberRow/MemberRow";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { popWorkspaceMemberPickerModal } from "@/slice/modalSlice";
import { WorkspaceMemberDto } from "@/be/jinear-core";
import { useAppDispatch } from "@/store/store";

interface WorkspaceMembersTabProps {
  channelId: string;
  workspaceId: string;
}

const WorkspaceMembersTab: React.FC<WorkspaceMembersTabProps> = ({ channelId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { data: retrieveChannelMembersResponse, isFetching } = useRetrieveChannelMembersQuery({ channelId });
  const [addChannelMember, { isLoading: isAddChannelMemberLoading }] = useAddChannelMemberMutation();

  const pickWorkspaceMember = () => {
    dispatch(popWorkspaceMemberPickerModal({ visible: true, workspaceId: workspaceId, onPick }));
  };

  const onPick = (pickedList: WorkspaceMemberDto[]) => {
    const pick = pickedList?.[0];
    if (pick) {
      addChannelMember({ channelId, accountId: pick.accountId });
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.channelActionBar}>
        <Button
          variant={ButtonVariants.contrast}
          heightVariant={ButtonHeight.short}
          onClick={pickWorkspaceMember}
          loading={isAddChannelMemberLoading}
          disabled={isAddChannelMemberLoading}
        >
          {t("addMemberToChannel")}
        </Button>
      </div>
      <div className={"spacer-h-1"} />
      {isFetching && <CircularLoading />}
      {retrieveChannelMembersResponse
        ?.data
        ?.map(channelMember => <MemberRow key={channelMember.channelMemberId} member={channelMember} />)}
    </div>
  );
};

export default WorkspaceMembersTab;