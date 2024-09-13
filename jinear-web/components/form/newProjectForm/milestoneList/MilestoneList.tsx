import React from "react";
import styles from "./MilestoneList.module.css";
import { MilestoneInitializeDto, ProjectInitializeRequest } from "@/be/jinear-core";
import { UseFormSetValue } from "react-hook-form";
import MilestoneListItem from "@/components/form/newProjectForm/milestoneListItem/MilestoneListItem";
import { UUID } from "@/utils/UUID";
import { useToggle } from "@/hooks/useToggle";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPlus } from "react-icons/lu";
import useTranslation from "@/locals/useTranslation";


export interface IMilestoneInitializeDto extends MilestoneInitializeDto {
  id: string;
}

interface MilestoneListProps {
  milestoneList?: IMilestoneInitializeDto[] | null;
  setValue: UseFormSetValue<ProjectInitializeRequest>;
}

const sortList = (list: IMilestoneInitializeDto[]) => {
  return [...list].sort((a, b) => (a.targetDate?.getTime() || Infinity) - (b.targetDate?.getTime() || Infinity));
};

const MilestoneList: React.FC<MilestoneListProps> = ({ milestoneList, setValue }) => {
  const { t } = useTranslation();
  const [milestoneInputVisible, toggleMilestoneInputVisible] = useToggle(false);

  const onRemove = (id: string, asInput?: boolean) => {
    if (asInput) {
      toggleMilestoneInputVisible();
      return;
    }
    const filtered = milestoneList?.filter(m => m.id != id);
    setValue("milestones", filtered);
  };

  const onTargetDateChange = (id: string, targetDate?: Date | null) => {
    const list = milestoneList?.map(m => {
      if (m.id == id) {
        return { ...m, targetDate };
      }
      return { ...m };
    });
    setValue("milestones", sortList(list as IMilestoneInitializeDto[]));
  };

  const onNewMilestoneAdd = (id: string, title?: string, targetDate?: Date | null) => {
    const curr = milestoneList ?? [];
    const next = sortList([...curr, {
      id,
      title,
      targetDate
    }] as IMilestoneInitializeDto[]) as MilestoneInitializeDto[];
    setValue("milestones", next);
  };

  return (
    <div className={styles.container}>
      <h3>{t("newProjectFormMilestonesTitle")}</h3>
      <div className={styles.listContainer}>
        {milestoneList?.map(milestoneInitDto =>
          <MilestoneListItem
            key={milestoneInitDto.id}
            id={milestoneInitDto.id}
            title={milestoneInitDto.title}
            onRemove={onRemove}
            targetDate={milestoneInitDto.targetDate}
            onTargetDateChange={onTargetDateChange}
          />)
        }
        {milestoneInputVisible ?
          <MilestoneListItem
            key={"new-input"}
            id={UUID()}
            onRemove={onRemove}
            onTargetDateChange={onTargetDateChange}
            asInput={true}
            onAdd={onNewMilestoneAdd}
          /> :
          <Button
            type={"button"}
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.filled}
            onClick={toggleMilestoneInputVisible}
            className={styles.newButton}
          >
            <LuPlus className="icon" />
            <span>{t("newProjectFormMilestonesNewButtonLabel")}</span>
          </Button>
        }
      </div>
    </div>
  );
};

export default MilestoneList;