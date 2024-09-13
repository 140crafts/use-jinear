import React, { useEffect, useState } from "react";
import styles from "./ProjectPriorityPickerButton.module.css";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { popProjectPrioritySelectModal } from "@/slice/modalSlice";
import { ProjectPriorityType } from "@/be/jinear-core";
import ProjectPriorityIcon from "@/components/projectPriorityIcon/ProjectPriorityIcon";

interface ProjectPriorityPickerButtonProps {
  label?: string;
  onPick?: (pick?: ProjectPriorityType | null) => void;
  initialPick?: ProjectPriorityType;
}

const ProjectPriorityPickerButton: React.FC<ProjectPriorityPickerButtonProps> = ({ label, initialPick, onPick }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [currentPick, setCurrentPick] = useState<ProjectPriorityType | null>();

  useEffect(() => {
    setCurrentPick(initialPick);
  }, [initialPick]);

  const popPicker = () => {
    dispatch(popProjectPrioritySelectModal({ visible: true, onPick: onPickerPick }));
  };

  const onPickerPick = (picked: ProjectPriorityType) => {
    setCurrentPick(picked);
    onPick?.(picked);
  };

  const deselect = () => {
    setCurrentPick(null);
    onPick?.(null);
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={currentPick != null}
        onPickClick={popPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            {currentPick &&
              <>
                <ProjectPriorityIcon projectPriority={currentPick} className={styles.icon} />
                <span>
                  {t(`projectPriority_${currentPick}`)}
                </span>
              </>
            }
          </div>
        }
        emptySelectionLabel={label ?? t("projectPriorityPickerButtonLabel")}
        onUnpickClick={deselect}
      />
    </div>
  );
};

export default ProjectPriorityPickerButton;