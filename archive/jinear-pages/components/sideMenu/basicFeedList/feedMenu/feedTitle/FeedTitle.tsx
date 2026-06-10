import Button, { ButtonVariants } from "@/components/button";
import { FeedDto, WorkspaceDto } from "@/model/be/jinear-core";
import { selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoEllipsisHorizontal, IoPeopleOutline } from "react-icons/io5";
import styles from "./FeedTitle.module.css";

interface FeedTitleProps {
  feed: FeedDto;
  workspace: WorkspaceDto;
}
const MAX_NAME_LENGTH = 20;

const FeedTitle: React.FC<FeedTitleProps> = ({ feed, workspace }) => {
  const { t } = useTranslation();
  const isAdmin = useTypedSelector(selectCurrentAccountsWorkspaceRoleIsAdminOrOwnerWithPlainWorkspaceDto(workspace));
  const feedUri = `/${workspace?.username}/tasks/feed/${feed?.feedId}`;
  const membersUri = `/${workspace?.username}/tasks/feed/${feed?.feedId}/members`;
  const settingsUri = `/${workspace?.username}/tasks/feed/${feed?.feedId}/settings`;

  return (
    <div className={styles.container}>
      <Button
        variant={ButtonVariants.hoverFilled2}
        href={feedUri}
        data-tooltip-right={feed.name?.length > MAX_NAME_LENGTH ? feed.name : undefined}
        className={styles.feedNameButton}
      >
        <b className={cn(styles.feedName, "line-clamp")}>
          {shortenStringIfMoreThanMaxLength({ text: feed.name, maxLength: MAX_NAME_LENGTH })}
        </b>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} href={membersUri} data-tooltip-right={t("sideMenuFeedMembers")}>
        <IoPeopleOutline />
      </Button>
      {isAdmin && (
        <Button variant={ButtonVariants.hoverFilled2} href={settingsUri} data-tooltip-right={t("sideMenuFeedSettings")}>
          <IoEllipsisHorizontal />
        </Button>
      )}
    </div>
  );
};

export default FeedTitle;
