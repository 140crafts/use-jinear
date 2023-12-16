import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility, popTeamPickerModal, popWorkspacePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
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
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const [updatePreferredWorkspace, { isLoading, isSuccess }] = useUpdatePreferredWorkspaceMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isLoading }));
  }, [isLoading]);

  const onWorkspacePick = (workspace: WorkspaceDto) => {
    updatePreferredWorkspace({ workspaceId: workspace.workspaceId });
  };

  const onTeamPick = (team: TeamDto) => {
    onTeamChange?.(team);
  };

  const popChangeWorkspaceModal = () => {
    dispatch(popWorkspacePickerModal({ currentWorkspaceId, onPick: onWorkspacePick, visible: true }));
  };

  const popChangeTeamModal = () => {
    dispatch(popTeamPickerModal({ visible: true, workspaceId: workspace.workspaceId, onPick: onTeamPick }));
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
          onClick={popChangeWorkspaceModal}
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
