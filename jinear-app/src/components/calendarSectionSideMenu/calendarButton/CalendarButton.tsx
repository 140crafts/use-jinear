import Button, { ButtonVariants } from "@/components/button";
import type { CalendarDto, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store";
import { shortenStringIfMoreThanMaxLength } from "@/util/textUtil";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal, IoPeopleOutline } from "react-icons/io5";
import styles from "./CalendarButton.module.css";
import {useLocation} from "react-router-dom";

interface CalendarButtonProps {
  workspace: WorkspaceDto;
  calendar?: CalendarDto;
  team?: TeamDto;
}

const CalendarButton: React.FC<CalendarButtonProps> = ({ workspace, calendar, team }) => {
  const { t } = useTranslation();
  const isAdmin = useTypedSelector(selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto(workspace));

  const {pathname} = useLocation();

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
        variant={membersPath == pathname ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        href={membersPath}
        data-tooltip-right={t("sideMenuCalendarMembers")}
      >
        <IoPeopleOutline />
      </Button>
      {isAdmin && (
        <Button
          variant={settingsPath == pathname ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
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
