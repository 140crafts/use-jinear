import React, { useEffect, useRef } from "react";
import styles from "./MilestoneDetail.module.scss";
import { MilestoneDto, ProjectDto } from "@/be/jinear-core";
import MilestoneDescription
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneDescription/MilestoneDescription";
import MilestoneTitle
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneTitle/MilestoneTitle";
import MilestoneTargetDate
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneTargetDate/MilestoneTargetDate";
import useTranslation from "@/locals/useTranslation";
import { LuDiamond } from "react-icons/lu";
import cn from "classnames";
import Button, { ButtonHeight } from "@/components/button";
import MilestoneDeleteButton
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneDeleteButton/MilestoneDeleteButton";
import { IoCaretDown, IoCaretForward } from "react-icons/io5";
import { useToggle } from "@/hooks/useToggle";
import MilestoneStateToggleButton
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneStateToggleButton/MilestoneStateToggleButton";
import MilestoneTasks
  from "@/components/projectDetailScreen/projectDetailMilestones/milestoneDetail/milestoneTasks/MilestoneTasks";

interface MilestoneDetailProps {
  workspaceId: string;
  milestone: MilestoneDto;
  project: ProjectDto;
  isFirst: boolean;
  isLast: boolean;
  isOneBeforeLast: boolean;
}

const MilestoneDetail: React.FC<MilestoneDetailProps> = ({
                                                           isFirst,
                                                           isLast,
                                                           isOneBeforeLast,
                                                           workspaceId,
                                                           milestone,
                                                           project
                                                         }) => {
  const { t } = useTranslation();
  const [collapsed, toggleCollapsed, setCollapsed] = useToggle(milestone.milestoneState == "COMPLETED");
  const changedCollapsedStatusAtLeastOnce = useRef<boolean>(false);

  useEffect(() => {
    if (!changedCollapsedStatusAtLeastOnce.current) {
      setCollapsed(milestone.milestoneState == "COMPLETED");
    }
  }, [setCollapsed, milestone]);

  const toggleCollapseView = () => {
    changedCollapsedStatusAtLeastOnce.current = true;
    toggleCollapsed();
  };

  return (
    <div className={cn(styles.container, isLast && styles.lastContainer)}>

      <div className={styles.iconContainer}>
        <LuDiamond className={cn("icon", styles.icon, isFirst && styles.firstIcon)} />
        {!isLast && <div className={styles.line} />}
        {!isLast && <div className={cn(styles.hoveredLine, !isOneBeforeLast && styles.initialHoveredLine)} />}
      </div>

      <div className={styles.contentContainer}>
        <div className={styles.milestoneInfoContainer}>

          <div className={styles.milestoneTitleContainer}>
            <MilestoneTitle
              milestoneId={milestone.milestoneId}
              title={milestone.title}
              milestoneState={milestone.milestoneState}
            />
            <div className={"spacer-w-1"} />
            <Button
              className={styles.toggleViewButton}
              heightVariant={ButtonHeight.short}
              onClick={toggleCollapseView}
            >
              {collapsed ? <IoCaretForward className={"icon"} /> : <IoCaretDown className={"icon"} />}
            </Button>

            <div className={"flex-1"} />
            <div className={"spacer-w-1"} />

            <div className={styles.milestoneActionsContainer}>
              <MilestoneStateToggleButton
                milestoneId={milestone.milestoneId}
                milestoneState={milestone.milestoneState}
              />
              <MilestoneTargetDate
                milestoneId={milestone.milestoneId}
                targetDate={milestone.targetDate}
              />
              <MilestoneDeleteButton
                milestoneId={milestone.milestoneId}
                title={milestone.title}
              />
            </div>
          </div>

          {!collapsed &&
            <MilestoneDescription milestoneId={milestone.milestoneId} description={milestone.description} />}
        </div>

        {!collapsed &&
          <MilestoneTasks workspaceId={workspaceId} project={project} milestone={milestone} />}

        <div className={"spacer-h-2"} />
      </div>
    </div>
  );
};

export default MilestoneDetail;