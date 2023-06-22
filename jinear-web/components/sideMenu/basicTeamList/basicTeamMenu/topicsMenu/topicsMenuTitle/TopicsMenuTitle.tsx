import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { IoAdd, IoPricetagOutline } from "react-icons/io5";
import styles from "./TopicsMenuTitle.module.css";

interface TopicsMenuTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TopicsMenuTitle: React.FC<TopicsMenuTitleProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const router = useRouter();

  return (
    <div className={styles.container}>
      <Button
        className={styles.labelButton}
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace?.username}/${team?.username}/topic/list`}
      >
        <IoPricetagOutline />
        <div>{t("sideMenuTeamTopics")}</div>
      </Button>
      <div className={styles.actionButtonsContainer}>
        <Button
          variant={ButtonVariants.hoverFilled2}
          heightVariant={ButtonHeight.short}
          href={`/${workspace?.username}/${team?.username}/topic/new`}
          data-tooltip-right={t("sideMenuTeamTopicsNew")}
        >
          <IoAdd />
        </Button>
      </div>
    </div>
  );
};

export default TopicsMenuTitle;
