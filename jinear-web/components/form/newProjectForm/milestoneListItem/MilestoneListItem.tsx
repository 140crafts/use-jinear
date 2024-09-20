import React, { ChangeEvent, useEffect, useRef, useState } from "react";
import styles from "./MilestoneListItem.module.scss";
import { LuCalendarPlus, LuCheck, LuDiamond, LuX } from "react-icons/lu";
import DatePickerButton, { IDatePickerButtonRef } from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonVariants } from "@/components/button";
import toast from "react-hot-toast";
import Logger from "@/utils/logger";

interface MilestoneListItemProps {
  id: string,
  title?: string;
  onRemove: (id: string, asInput?: boolean) => void,
  targetDate?: Date | null
  onTargetDateChange?: (id: string, date?: Date | null) => void,
  asInput?: boolean
  onAdd?: (id: string, title?: string, targetDate?: Date | null) => void
}

const logger = Logger("MilestoneListItem");

const MilestoneListItem: React.FC<MilestoneListItemProps> = ({
                                                               id,
                                                               title,
                                                               targetDate,
                                                               asInput = false,
                                                               onRemove,
                                                               onTargetDateChange,
                                                               onAdd
                                                             }) => {
  const { t } = useTranslation();
  const [input, setInput] = useState<string | undefined>(title || "");
  const [date, setDate] = useState<Date | null | undefined>(targetDate);
  const datePickerButtonRef = useRef<IDatePickerButtonRef>(null);

  logger.log({ id, title, targetDate, input, date, asInput });

  useEffect(() => {
    setInput(title || "");
  }, [title]);

  useEffect(() => {
    setDate(targetDate);
  }, [targetDate]);

  const onRemoveClick = () => {
    onRemove?.(id, asInput);
  };

  const onDateChange = (date?: Date | null) => {
    setDate(date);
    !asInput && onTargetDateChange?.(id, date);
  };

  const onTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setInput?.(value);
  };

  const onAddClick = () => {
    if (!input || input?.length == 0) {
      toast(t("milestoneListItemTitleCanNotBeEmpty"));
      return;
    }
    onAdd?.(id, input, date);
    setDate(undefined);
    setInput("");
    datePickerButtonRef?.current?.reset?.();
  };

  const isEnter = (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.key === "Enter") {
      onAddClick?.();
      event.preventDefault?.();
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.titleContainer}>
        <LuDiamond className={"icon"} />
        {asInput ?
          <input
            type={"text"}
            className={"flex-1"}
            value={input}
            onChange={onTitleChange}
            onKeyDown={isEnter}
            placeholder={t("newMilestoneTitlePlaceholder")}
          />
          : <span className={"flex-1"}>{title}</span>
        }
      </div>

      <div className={styles.milestoneActionButtonsContainer}>
        <DatePickerButton
          ref={datePickerButtonRef}
          title={t("newProjectFromMilestoneListItemTargetDateButtonLabel")}
          emptySelectionComponent={<LuCalendarPlus className={"icon"} />}
          initialDate={date}
          onDateChange={onDateChange}
        />
        {!date && <Button variant={ButtonVariants.hoverFilled2} onClick={onRemoveClick}><LuX /></Button>}
        {asInput && <Button variant={ButtonVariants.hoverFilled2} onClick={onAddClick}><LuCheck /></Button>}
      </div>
    </div>
  );
};

export default MilestoneListItem;