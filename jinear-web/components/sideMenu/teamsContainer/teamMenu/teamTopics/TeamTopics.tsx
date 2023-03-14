import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./TeamTopics.module.css";
interface TeamTopicsProps {
  teamId: string;
}

const SLICE_SIZE = 5;

const TeamTopics: React.FC<TeamTopicsProps> = ({ teamId }) => {
  const router = useRouter();
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const {
    data: teamTopicListingResponse,
    isSuccess,
    isError,
    isLoading,
  } = useRetrieveTeamTopicsQuery(teamId, { skip: teamId == null });

  const totalElements = teamTopicListingResponse?.data?.totalElements || 0;
  const remainingCount = totalElements - SLICE_SIZE;
  const moreButtonLabel = t("sideMenuTeamTopicsMore").replace("${number}", `${remainingCount}`);

  const popNewTopicModal = () => {
    router.push(`/${preferredWorkspace?.username}/${preferredTeam?.name}/topic/new`);
  };

  const routeTopicList = () => {
    router.push(`/${preferredWorkspace?.username}/${preferredTeam?.name}/topic/list`);
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle
        label={t("sideMenuTeamTopics")}
        hasDetailButton={true}
        hasAddButton={true}
        onAddButtonClick={popNewTopicModal}
        onDetailButtonClick={routeTopicList}
        buttonVariant={isSuccess && totalElements == 0 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
      />
      <div className={styles.topicListContainer}>
        {isSuccess && totalElements == 0 && <div className={styles.noTeamLabel}>{t("sideMenuTeamNoTopics")}</div>}
        {teamTopicListingResponse?.data?.content?.slice(0, SLICE_SIZE).map((topic) => (
          <Button
            key={topic.topicId}
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.short}
            className={cn(styles.button)}
            data-tooltip-multiline={topic.name.length > 12 ? topic.name : undefined}
          >
            {topic.name.length > 12 ? `${topic.name.substring(0, 12)}...` : topic.name}
          </Button>
        ))}

        {remainingCount > 0 && (
          <Button
            href={`/${preferredWorkspace?.username}/${preferredTeam?.name}/topic/list`}
            variant={ButtonVariants.filled2}
            className={styles.button}
            heightVariant={ButtonHeight.short}
            style={{ opacity: 0.5 }}
          >
            {moreButtonLabel}
          </Button>
        )}
      </div>
    </div>
  );
};

export default TeamTopics;
