import React, { useEffect } from "react";
import styles from "./NewCalendarEventForm.module.css";
import cn from "classnames";
import { SubmitHandler, useForm } from "react-hook-form";
import { CalendarEventInitializeRequest } from "@/be/jinear-core";
import ExternalCalendarAndSourcePicker
  from "@/components/externalCalendarAndSourcePicker/ExternalCalendarAndSourcePicker";
import TitleInput from "@/components/form/newCalendarEventForm/titleInput/TitleInput";
import EventDateButtons from "@/components/eventDateButtons2/EventDateButtons";
import Logger from "@/utils/logger";
import { useRetrieveCalendarMembershipsQuery } from "@/api/calendarMemberApi";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useInsertEventMutation } from "@/api/calendarEventApi";
import useTranslation from "@/locals/useTranslation";
import toast from "react-hot-toast";

interface NewCalendarEventFormProps {
  className?: string;
  workspaceId: string,
  onClose?: () => void
}

const logger = Logger("NewCalendarEventForm");

const NewCalendarEventForm: React.FC<NewCalendarEventFormProps> = ({
                                                                     className,
                                                                     workspaceId,
                                                                     onClose
                                                                   }) => {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    setFocus,
    setValue,
    watch,
    reset,
    formState: { errors }
  } = useForm<CalendarEventInitializeRequest>();

  const calendarId = watch("calendarId");
  const calendarSourceId = watch("calendarSourceId");
  const assignedDate = watch("assignedDate");
  const dueDate = watch("dueDate");
  const hasPreciseAssignedDate = watch("hasPreciseAssignedDate");
  const hasPreciseDueDate = watch("hasPreciseDueDate");
  const allDay = !(hasPreciseAssignedDate || hasPreciseDueDate);

  logger.log({
    calendarId,
    calendarSourceId,
    assignedDate,
    dueDate,
    hasPreciseAssignedDate,
    hasPreciseDueDate,
    allDay
  });

  const { data: membershipsResponse } = useRetrieveCalendarMembershipsQuery({ workspaceId });
  const [insertEvent, {
    data: insertResponse,
    isLoading: isInsertEventLoading,
    isSuccess: isInsertSuccess
  }] = useInsertEventMutation();

  useEffect(() => {
    const firstCalendar = membershipsResponse?.data?.[0]?.calendar;
    if (firstCalendar) {
      setValue("calendarId", firstCalendar.calendarId);
      const calendarSourceId = firstCalendar.calendarSources?.filter(source => !source.readOnly)?.[0].externalCalendarSourceId;
      calendarSourceId && setValue("calendarSourceId", calendarSourceId);
    }
  }, [setValue, membershipsResponse]);

  useEffect(() => {
    setValue("assignedDate", new Date());
    setValue("dueDate", new Date());
    setValue("hasPreciseAssignedDate", false);
    setValue("hasPreciseDueDate", false);
  }, [setValue]);

  useEffect(() => {
    logger.log({ titleErrorMessage: errors.summary?.message });
    if (errors.summary?.message) {
      toast(errors.summary.message, {
        position: window.innerWidth < 768 ? "top-center" : "bottom-center",
        duration: 6000
      });
    }
  }, [errors.summary]);

  const submit: SubmitHandler<CalendarEventInitializeRequest> = (data) => {
    logger.log({ submit: data });
    const req = {
      ...data,
      assignedDate: data.assignedDate?.toISOString(),
      dueDate: data.dueDate?.toISOString()
    };
    //@ts-ignore
    insertEvent(req);
  };

  useEffect(() => {
    if (isInsertSuccess && insertResponse) {
      logger.log({ newCalendarEventForm: insertResponse });
      toast(t("newEventInsertSuccess"));
      reset?.();
      onClose?.();
    }
  }, [isInsertSuccess, insertResponse]);

  const onCloseRequested = () => {
    reset?.();
    onClose?.();
  };

  const onAssignedDateUpdate = (assignedDate: Date) => {
    assignedDate && setValue("assignedDate", assignedDate);
  };

  const onDueDateUpdate = (dueDate: Date) => {
    dueDate && setValue("dueDate", dueDate);
  };

  const onAllDayUpdate = (allDay: boolean) => {
    logger.log({ onAllDayUpdate: allDay });
    setValue("hasPreciseAssignedDate", allDay);
    setValue("hasPreciseDueDate", allDay);
  };

  const onCalendarSelect = (calendarId: string) => {
    calendarId && setValue("calendarId", calendarId);
  };

  const onCalendarSourceSelect = (calendarSourceId: string) => {
    calendarSourceId && setValue("calendarSourceId", calendarSourceId);
  };

  return (
    <form
      autoComplete="off"
      id={"new-calendar-event-form"}
      className={cn(styles.form, className)}
      onSubmit={handleSubmit(submit)}
      action="#"
    >
      <div className={styles.formContent}>
        <TitleInput labelClass={styles.label} register={register} />

        <div className="flex-1" />

        <ExternalCalendarAndSourcePicker
          calendarList={membershipsResponse?.data?.map(membership => membership.calendar) || []}
          selectedCalendarId={calendarId}
          selectedCalendarSourceId={calendarSourceId}
          onCalendarSelect={onCalendarSelect}
          onCalendarSourceSelect={onCalendarSourceSelect}
          hasNewEventText={false}
        />

        <div className={styles.gap} />

        <EventDateButtons
          allDay={allDay}
          assignedDate={assignedDate || new Date()}
          dueDate={dueDate || new Date()}
          onAssignedDateUpdate={onAssignedDateUpdate}
          onDueDateUpdate={onDueDateUpdate}
          onAllDayUpdate={onAllDayUpdate}
        />
      </div>

      <div className={styles.footerContainer}>
        <div className="flex-1" />
        <div className={styles.footerActionButtonContainer}>
          <Button
            disabled={isInsertEventLoading}
            onClick={onCloseRequested}
            className={styles.footerButton}
            heightVariant={ButtonHeight.short}
          >
            {t("newTaskModalCalendarEventCancel")}
          </Button>
          <Button
            type="submit"
            disabled={isInsertEventLoading}
            loading={isInsertEventLoading}
            className={styles.footerButton}
            variant={ButtonVariants.contrast}
            heightVariant={ButtonHeight.short}
            progessClassname={styles.loadingButton}
          >
            {t("newTaskModalCalendarEventCreate")}
          </Button>
        </div>
      </div>
    </form>
  );
};

export default NewCalendarEventForm;