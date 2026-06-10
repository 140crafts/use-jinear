import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";

import React from "react";
import {IoAdd, IoPricetagOutline} from "react-icons/io5";
import styles from "./TopicsMenuTitle.module.css";
import {useLocation} from "react-router-dom";

interface TopicsMenuTitleProps {
    workspace: WorkspaceDto;
    team: TeamDto;
}

const TopicsMenuTitle: React.FC<TopicsMenuTitleProps> = ({workspace, team}) => {
    const {t} = useTranslation();
    const {pathname} = useLocation();
    const topicsPath = `/${workspace.username}/tasks/${team.username}/topic/list`;

    return (
        <div className={styles.container}>
            <Button
                className={styles.labelButton}
                variant={pathname == topicsPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
                href={`/${workspace?.username}/tasks/${team?.username}/topic/list`}
            >
                <IoPricetagOutline/>
                <div>{t("sideMenuTeamTopics")}</div>
            </Button>
            <div className={styles.actionButtonsContainer}>
                {team.teamState == "ACTIVE" && (
                    <Button
                        variant={ButtonVariants.hoverFilled2}
                        heightVariant={ButtonHeight.short}
                        href={`/${workspace?.username}/tasks/${team?.username}/topic/new`}
                        data-tooltip-right={t("sideMenuTeamTopicsNew")}
                    >
                        <IoAdd/>
                    </Button>
                )}
            </div>
        </div>
    );
};

export default TopicsMenuTitle;
