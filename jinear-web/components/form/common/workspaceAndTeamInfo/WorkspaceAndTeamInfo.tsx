import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTeamPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { shortenStringIfMoreThanMaxLength } from "@/utils/textUtil";
import cn from "classnames";
import React from "react";
import { IoHomeOutline, IoPeopleOutline } from "react-icons/io5";
import { LuChevronRight } from "react-icons/lu";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team?: TeamDto;
  workspaceTitle?: string;
  onTeamChange?: (team: TeamDto) => void;
  readOnly?: boolean;
  buttonContainerClassName?: string;
  heightVariant?: string;
  omitTeamButton?: boolean;
  additionalLabel?: string;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({
                                                                     workspace,
                                                                     team,
                                                                     workspaceTitle,
                                                                     onTeamChange,
                                                                     readOnly = false,
                                                                     buttonContainerClassName,
                                                                     heightVariant = ButtonHeight.default,
                                                                     omitTeamButton = false,
                                                                     additionalLabel
                                                                   }) => {
  const dispatch = useAppDispatch();

  const onTeamPick = (team: TeamDto) => {
    onTeamChange?.(team);
  };

  const popChangeTeamModal = () => {
    dispatch(
      popTeamPickerModal({
        visible: true,
        workspaceId: workspace.workspaceId,
        filterActiveTeams: true,
        onPick: onTeamPick
      })
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
          data-tooltip={workspace.title}
        >
          <IoHomeOutline className={styles.icon} />
          <b className="single-line">{shortenStringIfMoreThanMaxLength({
            text: workspace.title || "",
            maxLength: 12
          })}</b>
        </Button>
        {!omitTeamButton && team && <>
          <LuChevronRight className={styles.icon} />
          <Button
            disabled={readOnly}
            className={styles.button}
            variant={ButtonVariants.filled}
            heightVariant={heightVariant}
            onClick={popChangeTeamModal}
          >
            <IoPeopleOutline className={styles.icon} />
            <b className="single-line">{shortenStringIfMoreThanMaxLength({ text: team?.name || "", maxLength: 24 })}</b>
          </Button>
        </>}
        {additionalLabel && (
          <>
            <LuChevronRight className={styles.icon} />
            <b className="single-line">{additionalLabel}</b>
          </>
        )}
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
