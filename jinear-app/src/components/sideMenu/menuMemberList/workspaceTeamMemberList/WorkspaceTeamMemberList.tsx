import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {PageDto, TeamDto, TeamMemberDto, WorkspaceDto} from "@/model/be/jinear-core";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import {IoEllipsisHorizontal} from "react-icons/io5";
import MemberProfilePictureList from "../memberProfilePictureList/MemberProfilePictureList";
import styles from "./WorkspaceTeamMemberList.module.css";
import {useRetrieveTeamMembersQuery} from "@/api/teamMemberApi.ts";

interface WorkspaceTeamMemberListProps {
    workspace: WorkspaceDto;
    team: TeamDto;
}

const WorkspaceTeamMemberList: React.FC<WorkspaceTeamMemberListProps> = ({workspace, team}) => {
    const {t} = useTranslation();
    const {data: teamMemberResponse} = useRetrieveTeamMembersQuery({teamId: team.teamId});

    return (
        <div className={styles.container}>
            <Button
                href={`/${workspace?.username}/tasks/${team?.username}/members`}
                variant={ButtonVariants.hoverFilled2}
                heightVariant={ButtonHeight.short}
                className={styles.button}
            >
                <MemberProfilePictureList
                    accountList={teamMemberResponse?.data.content.map((member) => member.account) ?? []} type="team"/>
            </Button>
        </div>
    );
};

export default WorkspaceTeamMemberList;
