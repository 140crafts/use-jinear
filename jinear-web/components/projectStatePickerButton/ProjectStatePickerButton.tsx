import React, { useEffect, useState } from "react";
import styles from "./ProjectStatePickerButton.module.css";
import useTranslation from "@/locals/useTranslation";
import { useAppDispatch } from "@/store/store";
import { ProjectStateType } from "@/be/jinear-core";
import { popProjectStateSelectModal } from "@/slice/modalSlice";
import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import ProjectStateIcon from "@/components/projectStateIcon/ProjectStateIcon";

interface ProjectStatePickerButtonProps {
  label?: string;
  onPick?: (pick?: ProjectStateType | null) => void;
  initialPick?: ProjectStateType;
}

const ProjectStatePickerButton: React.FC<ProjectStatePickerButtonProps> = ({ label, initialPick, onPick }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [currentPick, setCurrentPick] = useState<ProjectStateType | null>();

  useEffect(() => {
    setCurrentPick(initialPick);
  }, [initialPick]);

  const popPicker = () => {
    dispatch(popProjectStateSelectModal({ visible: true, onPick: onPickerPick }));
  };

  const onPickerPick = (picked: ProjectStateType) => {
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
                <ProjectStateIcon projectState={currentPick} className={styles.icon} />
                <span>
                  {t(`projectState_${currentPick}`)}
                </span>
              </>
            }
          </div>
        }
        emptySelectionLabel={label ?? t("projectStatePickerButtonLabel")}
        onUnpickClick={deselect}
      />
    </div>
  );
};

export default ProjectStatePickerButton;