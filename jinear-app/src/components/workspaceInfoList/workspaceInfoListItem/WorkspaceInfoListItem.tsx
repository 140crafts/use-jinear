import Button, {ButtonVariants} from "@/components/button";
import ProfilePhoto from "@/components/profilePhoto";
import type {WorkspaceDto} from "@/model/be/jinear-core";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";

import React from "react";
import styles from "./WorkspaceInfoListItem.module.css";
import {useNavigate} from "react-router-dom";
import {LuCheck} from "react-icons/lu";

interface WorkspaceInfoListItemProps {
    workspace: WorkspaceDto;
    isCurrent: boolean;
    onWorkspaceChangeComplete?: () => void;
}

const WorkspaceInfoListItem: React.FC<WorkspaceInfoListItemProps> = ({workspace, isCurrent, onWorkspaceChangeComplete}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();

    const changePrefferedWorkspace = () => {
        if (!isCurrent) {
            navigate(`/${workspace.username}`);
        }
        onWorkspaceChangeComplete?.();
    };

    return (
        <div className={styles.container}>
            <div className={styles.infoContainer}>
                <div
                    className={cn(
                        styles.profilePicContainer,
                        !workspace?.profilePicture?.url && styles["profilePicContainer-without-profile-pic"]
                    )}
                >
                    {workspace?.profilePicture?.url ? (
                        <ProfilePhoto
                            boringAvatarKey={workspace.workspaceId}
                            url={workspace?.profilePicture?.url}
                            wrapperClassName={styles.profilePic}
                            imgClassName={styles.profilePicImg}
                        />
                    ) : (
                        <div
                            className={styles.firstLetter}>{workspace.username.substring(0, 1)?.toLocaleUpperCase()}</div>
                    )}
                </div>
                <Button
                    className={styles.nameContainer}
                    variant={isCurrent ? ButtonVariants.filled : ButtonVariants.default}
                    onClick={changePrefferedWorkspace}
                >
                    <span className={cn(styles.title, "line-clamp-2")}>{workspace?.title}</span>
                    <div className="flex-1"/>
                    {isCurrent ? <LuCheck/> : <div>{"->"}</div>}
                </Button>
            </div>
        </div>
    );
};

export default WorkspaceInfoListItem;
