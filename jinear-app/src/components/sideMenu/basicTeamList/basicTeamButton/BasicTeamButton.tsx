import React from 'react';
import styles from './BasicTeamButton.module.css';
import Button, {ButtonVariants} from "@/components/button";
import cn from "classnames";
import {shortenStringIfMoreThanMaxLength} from "@/util/textUtil.ts";
import type {TeamDto, WorkspaceDto} from "@/be/jinear-core.ts";
import {useLocation} from "react-router-dom";

interface BasicTeamButtonProps {
    workspace: WorkspaceDto,
    team: TeamDto
}

const BasicTeamButton: React.FC<BasicTeamButtonProps> = ({workspace, team}) => {
    const {pathname} = useLocation()
    const homePath = `/${workspace?.username}/tasks/${team?.username}`;
    const atPath = pathname == homePath;

    return (
        <Button
            className={styles.teamButton}
            variant={atPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
            href={homePath}
        >
            <span className={cn(styles.teamName, atPath && 'bold', "single-line")}>
                {shortenStringIfMoreThanMaxLength({
                    text: team.name,
                    maxLength: 29,
                })}
            </span>
        </Button>
    );
}

export default BasicTeamButton;