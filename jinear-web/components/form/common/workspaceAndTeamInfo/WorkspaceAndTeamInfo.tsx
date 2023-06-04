import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility, popTeamOptionsModal, popWorkspacePickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoChevronForward } from "react-icons/io5";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  personalWorkspaceTitle: string;
  workspaceTitle: string;
  personalWorkspaceLabel: string;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({
  workspace,
  team,
  personalWorkspaceTitle,
  workspaceTitle,
  personalWorkspaceLabel,
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

  const popChangeWorkspaceModal = () => {
    dispatch(popWorkspacePickerModal({ currentWorkspaceId, onPick: onWorkspacePick, visible: true }));
  };

  const popChangeTeamModal = () => {
    dispatch(popTeamOptionsModal());
  };

  return (
    <div className={styles.container}>
      {workspace.isPersonal ? personalWorkspaceTitle : workspaceTitle}
      <div className={styles.buttonContainer}>
        <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popChangeWorkspaceModal}>
          <b>{workspace.isPersonal ? personalWorkspaceLabel : workspace.title}</b>
        </Button>
        {!workspace.isPersonal && (
          <>
            {/* <div className="spacer-w-1" /> */}
            <IoChevronForward size={15} />
            {/* <div className="spacer-w-1" /> */}
            <Button variant={ButtonVariants.filled} heightVariant={ButtonHeight.short} onClick={popChangeTeamModal}>
              <b>{team.name}</b>
            </Button>
          </>
        )}
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
