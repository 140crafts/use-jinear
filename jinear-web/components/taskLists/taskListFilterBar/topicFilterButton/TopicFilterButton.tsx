import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TopicDto } from "@/model/be/jinear-core";
import { popTopicPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPricetag } from "react-icons/io5";
import { useSelectedTopics, useSetSelectedTopics, useTeamId } from "../context/TaskListFilterBarContext";
import styles from "./TopicFilterButton.module.css";

interface TopicFilterButtonProps {}

const TopicFilterButton: React.FC<TopicFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const teamId = useTeamId();
  const selectedTopics = useSelectedTopics();
  const setSelectedTopics = useSetSelectedTopics();
  const isEmpty = selectedTopics?.length == 0;

  const onPick = (pickedList: TopicDto[]) => {
    setSelectedTopics?.(pickedList);
  };

  const popPicker = () => {
    dispatch(popTopicPickerModal({ visible: true, teamId, multiple: true, initialSelectionOnMultiple: selectedTopics, onPick }));
  };

  return (
    <Button
      heightVariant={ButtonHeight.short}
      variant={isEmpty ? ButtonVariants.filled : ButtonVariants.filled2}
      onClick={popPicker}
    >
      {isEmpty ? (
        t("taskFilterTopicFilterButtonEmpty")
      ) : (
        <b>
          {selectedTopics?.length == 1 ? (
            <div className={styles.singleItem}>
              <IoPricetag />
              {selectedTopics[0]?.name}
            </div>
          ) : (
            t("taskFilterTopicFilterButtonSelected")?.replace("${count}", `${selectedTopics?.length}`)
          )}
        </b>
      )}
    </Button>
  );
};

export default TopicFilterButton;
