import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamTopics.module.css";

interface TeamTopicsProps {
  teamId: string;
}

const SLICE_SIZE = 5;

const TeamTopics: React.FC<TeamTopicsProps> = ({ teamId }) => {
  const { t } = useTranslation();
  const {
    data: teamTopicListingResponse,
    isSuccess,
    isError,
    isLoading,
  } = useRetrieveTeamTopicsQuery(teamId, { skip: teamId == null });

  const totalElements = teamTopicListingResponse?.data?.totalElements || 0;
  const remainingCount = totalElements - SLICE_SIZE;
  const moreButtonLabel = t("sideMenuTeamTopicsMore").replace(
    "${number}",
    `${remainingCount}`
  );

  return (
    <div className={styles.container}>
      <MenuGroupTitle
        label="Topics"
        hasAddButton={true}
        buttonVariant={
          isSuccess && totalElements == 0
            ? ButtonVariants.filled2
            : ButtonVariants.hoverFilled2
        }
      />
      <div className={styles.topicListContainer}>
        {isSuccess && totalElements == 0 && (
          <div className={styles.noTeamLabel}>{t("sideMenuTeamNoTopics")}</div>
        )}
        {teamTopicListingResponse?.data?.content?.map((topic) => (
          <Button
            key={topic.topicId}
            variant={ButtonVariants.outline}
            heightVariant={ButtonHeight.mid}
            className={styles.button}
          >
            {topic.name}
          </Button>
        ))}

        {remainingCount > 0 && (
          <Button
            variant={ButtonVariants.filled2}
            className={styles.button}
            heightVariant={ButtonHeight.mid}
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
