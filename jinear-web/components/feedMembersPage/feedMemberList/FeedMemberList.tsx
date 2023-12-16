import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { FeedMemberDto, WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useAddFeedMemberMutation, useRetrieveFeedMembersQuery } from "@/store/api/feedMemberApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { popWorkspaceMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useMemo, useState } from "react";
import styles from "./FeedMemberList.module.scss";
import FeedMember from "./feedMember/FeedMember";

interface FeedMemberListProps {
  feedId: string;
  workspaceId: string;
}

const FeedMemberList: React.FC<FeedMemberListProps> = ({ feedId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const { data: feedMembersResponse, isFetching, isLoading } = useRetrieveFeedMembersQuery({ feedId });
  const [addFeedMember, { isLoading: isAddFeedMemberLoading }] = useAddFeedMemberMutation();
  const [page, setPage] = useState<number>(0);

  const isFeedOwner = useMemo(
    () => currentAccountId == feedMembersResponse?.data?.content?.[0]?.feed.initializedBy,
    [feedMembersResponse, currentAccountId]
  );

  const pickWorkspaceMember = () => {
    dispatch(popWorkspaceMemberPickerModal({ visible: true, workspaceId: workspaceId, onPick }));
  };

  const onPick = (pickedList: WorkspaceMemberDto[]) => {
    const pick = pickedList?.[0];
    addFeedMember({ feedId, accountId: pick.accountId });
  };

  const renderItem = (data: FeedMemberDto, index: number) => {
    return (
      <FeedMember
        key={`feed-member-${data.feedMemberId}`}
        data={data}
        isFeedOwner={isFeedOwner}
        currentAccountId={currentAccountId}
      />
    );
  };

  return (
    <div className={styles.container}>
      <SectionTitle title={t("feedMemberListTitle")} description={t("feedMemberListText")} />
      <div className={styles.actionBar}>
        <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={pickWorkspaceMember}>
          {t("feedMemberAddMember")}
        </Button>
      </div>
      <PaginatedList
        id={"feed-member-list"}
        data={feedMembersResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("feedMemberListEmptyLabel")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
      />
    </div>
  );
};

export default FeedMemberList;
