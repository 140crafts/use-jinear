import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import PaginatedList from "@/components/paginatedList/PaginatedList";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { CalendarMemberDto, WorkspaceMemberDto } from "@/model/be/jinear-core";
import { useAddCalendarMemberMutation, useRetrieveCalendarMembersQuery } from "@/store/api/calendarMemberApi";
import { popWorkspaceMemberPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./CalendarMemberList.module.css";
import CalendarMember from "./calendarMember/CalendarMember";

interface CalendarMemberListProps {
  calendarId: string;
  workspaceId: string;
}

const CalendarMemberList: React.FC<CalendarMemberListProps> = ({ calendarId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const { data: calendarMembersResponse, isFetching, isLoading } = useRetrieveCalendarMembersQuery({ calendarId });
  const [addCalendarMember, { isLoading: isAddCalendarMemberLoading }] = useAddCalendarMemberMutation();
  const [page, setPage] = useState<number>(0);

  const pickWorkspaceMember = () => {
    dispatch(popWorkspaceMemberPickerModal({ visible: true, workspaceId: workspaceId, onPick }));
  };

  const onPick = (pickedList: WorkspaceMemberDto[]) => {
    const pick = pickedList?.[0];
    addCalendarMember({ calendarId, accountId: pick.accountId });
  };

  const renderItem = (data: CalendarMemberDto, index: number) => {
    return (
      <CalendarMember
        key={`calendar-member-${data.calendarMemberId}`}
        data={data}
      />
    );
  };

  return (
    <div className={styles.container}>
      <SectionTitle title={t("calendarMemberListTitle")} description={t("calendarMemberListText")} />
      <div className={styles.actionBar}>
        <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={pickWorkspaceMember}>
          {t("calendarMemberAddMember")}
        </Button>
      </div>
      <PaginatedList
        id={"calendar-member-list"}
        data={calendarMembersResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("calendarMemberListEmptyLabel")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
      />
    </div>
  );
};

export default CalendarMemberList;
