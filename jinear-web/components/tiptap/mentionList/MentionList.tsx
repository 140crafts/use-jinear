import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useImperativeHandle, useState } from "react";
import styles from "./MentionList.module.scss";

interface MentionListProps {
  mentionListRect: DOMRect | null | undefined;
  items: string[];
  command: any;
}
const PADDING = 7;

const logger = Logger("MentionList");

export const MentionList = React.forwardRef<any, MentionListProps>(({ mentionListRect, items, command }, ref) => {
  const { t } = useTranslation();
  const [selectedIndex, setSelectedIndex] = useState<number>(0);

  useImperativeHandle(ref, () => ({
    onKeyDown: ({ event }: any) => {
      logger.log({ onKeyDown: event?.key });
      if (event?.key === "ArrowUp") {
        upHandler();
        return true;
      }
      if (event?.key === "ArrowDown") {
        downHandler();
        return true;
      }
      if (event?.key === "Enter") {
        enterHandler();
        return true;
      }
      return false;
    },
  }));

  const selectItem = (index: number) => {
    const item = items[index];
    if (item) {
      command({ id: item });
    }
  };

  const upHandler = () => {
    const next = (selectedIndex - 1 + items.length) % items.length;
    logger.log({ upHandler: next, selectedIndex });
    setSelectedIndex(next);
  };

  const downHandler = () => {
    setSelectedIndex((selectedIndex + 1) % items.length);
  };

  const enterHandler = () => {
    selectItem(selectedIndex);
  };

  useEffect(() => setSelectedIndex(0), [items]);

  return (
    <div
      ref={ref}
      className={styles.container}
      style={{
        display: mentionListRect ? "flex" : "none",
        left: mentionListRect?.left,
        top: mentionListRect ? mentionListRect.top + mentionListRect.height + PADDING : 0,
      }}
    >
      {items.length != 0 ? (
        items?.map((item, i) => (
          <div
            key={`mention-list-match-${item}`}
            className={cn(styles.listItem, selectedIndex == i && styles.selected)}
            onClick={() => selectItem(i)}
          >
            {item}
          </div>
        ))
      ) : (
        <div className={styles.listItem}>{t("mentionListNoResults")}</div>
      )}
    </div>
  );
});
