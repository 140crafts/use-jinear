import OrLine from "@/components/orLine/OrLine";
import { WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import React from "react";
import { UseFormSetValue } from "react-hook-form";
import WorkspaceTypeSelectButton from "../workspaceTypeSelectButton/WorkspaceTypeSelectButton";
import styles from "./WorkspaceTypeSelect.module.css";

interface WorkspaceTypeSelectProps {
  hasPersonalWorkspace: boolean;
  setIsPersonal: React.Dispatch<React.SetStateAction<boolean | undefined>>;
  setValue: UseFormSetValue<WorkspaceInitializeRequest>;
}

const WorkspaceTypeSelect: React.FC<WorkspaceTypeSelectProps> = ({ hasPersonalWorkspace, setIsPersonal, setValue }) => {
  const setPersonal = () => {
    setIsPersonal(true);
    setValue("isPersonal", true);
  };
  const setCollaborative = () => {
    setIsPersonal(false);
    setValue("isPersonal", false);
  };

  const onSearchClick = () => {};

  return (
    <div className={styles.workspaceTypePickerButtonsContainer}>
      {!hasPersonalWorkspace && <WorkspaceTypeSelectButton onClick={setPersonal} buttonType={"personal"} />}
      <WorkspaceTypeSelectButton onClick={setCollaborative} buttonType={"collaborative"} />
      <OrLine />
      <WorkspaceTypeSelectButton onClick={onSearchClick} buttonType={"search"} />
    </div>
  );
};

export default WorkspaceTypeSelect;
