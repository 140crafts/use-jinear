"use client";
import { AccountsWorkspacePerspectiveDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
import React from "react";
import { LuCalendarDays, LuCheckSquare, LuClipboardEdit, LuMessagesSquare, LuRss } from "react-icons/lu";
import Button, { ButtonVariants } from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import InboxButton from "./inboxButton/InboxButton";
import { useLiveQuery } from "dexie-react-hooks";
import { getUnreadConversationCount } from "../../repository/IndexedDbRepository";
import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountId } from "@/slice/accountSlice";

interface MainFeaturesSideMenuProps {
  workspace: AccountsWorkspacePerspectiveDto;
}

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  // const messagingEnabled = useFeatureFlag("MESSAGING") || isInGodModeWhitelist(currentAccountId);
  const messagingEnabled = false;
  const currentPath = usePathname();
  const calendarPath = `/${workspace?.username}/calendar`;
  const tasksPath = `/${workspace?.username}/tasks/projects`;
  const inboxPath = `/${workspace?.username}/inbox`;

  const conversationsPath = `/${workspace?.username}/conversations`;

  const unreadConversationCount = useLiveQuery(() => getUnreadConversationCount(workspace.workspaceId, currentAccountId)) ?? 0;
  const unreadConversationLabel = unreadConversationCount == 0 ? "" : unreadConversationCount > 99 ? "99+" : `${unreadConversationCount}`;

  return !workspace ? null : (
    <div className={styles.container}>
      <InboxButton
        isActive={inboxPath == currentPath}
        workspace={workspace}
        buttonStyle={styles.iconButton}
        iconStyle={styles.icon}
      />

      {messagingEnabled &&
        <Button
          className={styles.iconButton}
          href={conversationsPath}
          variant={currentPath?.indexOf(conversationsPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        >
          <div className={styles.iconContainer}>
            <LuMessagesSquare className={styles.icon} />
            {unreadConversationCount != 0 &&
              <div className={styles.unreadWrapper}> {unreadConversationLabel}</div>}
          </div>
          {t("mainFeaturesMenuLabelConversations")}
        </Button>
      }

      <Button
        className={styles.iconButton}
        href={calendarPath}
        variant={currentPath?.indexOf(calendarPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuCalendarDays className={styles.icon} />
        {t("mainFeaturesMenuLabelCalendar")}
      </Button>

      <Button
        className={styles.iconButton}
        href={tasksPath}
        variant={currentPath?.indexOf(tasksPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuCheckSquare className={styles.icon} />
        {t("mainFeaturesMenuLabelTasks")}
      </Button>

    </div>
  );
};

export default MainFeaturesSideMenu;
