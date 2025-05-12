import Button, { ButtonVariants } from "@/components/button";
import { CalendarDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
import React from "react";
import { IoEllipsisHorizontal, IoPeopleOutline } from "react-icons/io5";
import styles from "./CalendarButton.module.css";

interface CalendarButtonProps {
  workspace: WorkspaceDto;
  calendar?: CalendarDto;
  team?: TeamDto;
}

const CalendarButton: React.FC<CalendarButtonProps> = ({ workspace, calendar, team }) => {
  const { t } = useTranslation();
  const isAdmin = useTypedSelector(selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto(workspace));

  const currentPath = usePathname();

  const calendarName = calendar ? calendar.name : team ? team.name : "";

  const membersPath = calendar
    ? `/${workspace?.username}/calendar/${calendar.calendarId}/members`
    : team
    ? `/${workspace?.username}/tasks/${team.username}/members`
    : undefined;

  const settingsPath = calendar
    ? `/${workspace?.username}/calendar/${calendar.calendarId}/settings`
    : team
    ? `/${workspace?.username}/tasks/${team.username}/settings`
    : undefined;

  return (
    <div className={styles.container}>
      <b className={cn(styles.calendarName, "single-line")}>
        {shortenStringIfMoreThanMaxLength({
          text: calendarName,
          maxLength: 29,
        })}
      </b>
      <Button
        variant={membersPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        href={membersPath}
        data-tooltip-right={t("sideMenuCalendarMembers")}
      >
        <IoPeopleOutline />
      </Button>
      {isAdmin && (
        <Button
          variant={settingsPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
          href={settingsPath}
          data-tooltip-right={t("sideMenuCalendarSettings")}
        >
          <IoEllipsisHorizontal />
        </Button>
      )}
    </div>
  );
};

export default CalendarButton;
