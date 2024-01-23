import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import cn from "classnames";
import React from "react";
import { IoHomeOutline, IoPeopleOutline } from "react-icons/io5";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  workspaceTitle?: string;
  onTeamChange?: (team: TeamDto) => void;
  readOnly?: boolean;
  buttonContainerClassName?: string;
  heightVariant?: string;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({
  workspace,
  team,
  workspaceTitle,
  onTeamChange,
  readOnly = false,
  buttonContainerClassName,
  heightVariant = ButtonHeight.default,
}) => {
  const dispatch = useAppDispatch();

  const onTeamPick = (team: TeamDto) => {
    onTeamChange?.(team);
  };

  const popChangeTeamModal = () => {
    dispatch(
      popTeamPickerModal({ visible: true, workspaceId: workspace.workspaceId, filterActiveTeams: true, onPick: onTeamPick })
    );
  };

  return (
    <div className={styles.container}>
      {workspaceTitle && (
        <>
          {workspaceTitle}
          <div className="spacer-h-1" />
        </>
      )}
      <div className={cn(styles.buttonContainer, buttonContainerClassName)}>
        <Button
          //fixed
          disabled={true}
          className={styles.button}
          variant={ButtonVariants.filled}
          heightVariant={heightVariant}
        >
          <IoHomeOutline />
          <b>{workspace.title}</b>
        </Button>
        <Button
          disabled={readOnly}
          className={styles.button}
          variant={ButtonVariants.filled}
          heightVariant={heightVariant}
          onClick={popChangeTeamModal}
        >
          <IoPeopleOutline />
          <b>{team.name}</b>
        </Button>
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
