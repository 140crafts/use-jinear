import Button, { ButtonVariants } from "@/components/button";
import Line from "@/components/line/Line";
import { CalendarMemberDto } from "@/model/be/jinear-core";
import { useKickCalendarMemberMutation } from "@/store/api/calendarMemberApi";
import { closeDialogModal, popDialogModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { GiHighKick } from "react-icons/gi";
import styles from "./CalendarMember.module.scss";
import { selectCurrentAccountId } from "@/slice/accountSlice";

interface CalendarMemberProps {
  data: CalendarMemberDto;
}

const CalendarMember: React.FC<CalendarMemberProps> = ({ data }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [kickCalendarMember] = useKickCalendarMemberMutation();

  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const isLoggedInUserCalendarOwner = data.calendar.initializedBy == currentAccountId;
  const isCalendarMemberCalendarOwner = data.calendar.initializedBy == data.accountId;

  const kickMember = () => {
    kickCalendarMember({ calendarId: data.calendarId, accountId: data.accountId });
    dispatch(closeDialogModal());
  };

  const popAreYouSureModalForDeleteMember = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteCalendarMemberAreYouSureTitle"),
        content: t("deleteCalendarMemberAreYouSureText"),
        confirmButtonLabel: t("deleteCalendarMemberAreYouSureConfirmLabel"),
        onConfirm: kickMember
      })
    );
  };

  return (
    <>
      <div className={styles.container}>
        <div className={styles.titleContainer}>
          <div className={styles.role}>
            {isCalendarMemberCalendarOwner ? t("calendarMemberListItemOwner") : t(`calendarMemberListItemUser`)}
          </div>
          <div className={styles.title}>{data.account.email}</div>
        </div>

        {isLoggedInUserCalendarOwner && (currentAccountId != data.accountId) && (
          <div className={styles.rightInfoContainer}>
            <Button
              variant={ButtonVariants.filled}
              className={styles.button}
              onClick={popAreYouSureModalForDeleteMember}
              data-tooltip-right={t("activeCalendarMemberKick")}
            >
              <div className={styles.iconContainer}>
                <GiHighKick size={14} />
              </div>
            </Button>
          </div>
        )}
      </div>
      <Line className={styles.line} />
    </>
  );
};

export default CalendarMember;
