import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { TaskInitializeRequest, TeamDto, TopicDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popTopicPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import { IoPricetagOutline } from "react-icons/io5";
import styles from "./TopicPickerButton.module.css";

interface TopicPickerButtonProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
}

const TopicPickerButton: React.FC<TopicPickerButtonProps> = ({ workspace, team, setValue, register }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [selectedTopic, setSelectedTopic] = useState<TopicDto>();

  useEffect(() => {
    setValue("topicId", selectedTopic ? selectedTopic.topicId : "no-topic");
  }, [selectedTopic]);

  const onModalPick = (selection: TopicDto[]) => {
    if (selection.length != 0) {
      setSelectedTopic(selection[0]);
    }
  };

  const popTopicPicker = () => {
    dispatch(popTopicPickerModal({ visible: true, multiple: false, team, workspace, onPick: onModalPick }));
  };

  const deselect = () => {
    setSelectedTopic(undefined);
  };

  return (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={selectedTopic != null}
        onPickClick={popTopicPicker}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <IoPricetagOutline />
            {selectedTopic?.name}
          </div>
        }
        emptySelectionLabel={t("newTaskFormPickTopicButtonLabel")}
        onUnpickClick={deselect}
      />
      <input type="hidden" value={selectedTopic?.topicId || "no-topic"} {...register("topicId")} />
    </div>
  );
};

export default TopicPickerButton;
