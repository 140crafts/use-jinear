import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { queryStateAnyToStringConverter, queryStateArrayParser, useQueryState, useSetQueryState } from "@/hooks/useQueryState";
import { TopicDto } from "@/model/be/jinear-core";
import { useSearchTeamTopicsQuery } from "@/store/api/topicListingApi";
import { popTopicPickerModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoPricetag } from "react-icons/io5";
import { useTeam, useWorkspace } from "../context/TaskListFilterBarContext";
import styles from "./TopicFilterButton.module.css";

interface TopicFilterButtonProps {}

const logger = Logger("TopicFilterButton");
const TopicFilterButton: React.FC<TopicFilterButtonProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const workspace = useWorkspace();
  const team = useTeam();

  const setQueryState = useSetQueryState();
  const selectedTopicIds = useQueryState<string[]>("topicIds", queryStateArrayParser);
  const isEmpty = selectedTopicIds == null || selectedTopicIds.length == 0;

  const { data: searchResponse } = useSearchTeamTopicsQuery(
    { teamId: team?.teamId || "", nameOrTag: "" },
    { skip: team == null }
  );
  const selectedTopics = searchResponse?.data.filter(
    (topic) => selectedTopicIds && selectedTopicIds.indexOf(topic.topicId) != -1
  );
  logger.log({ selectedTopics, searchResponse, selectedTopicIds });
  const onPick = (pickedList: TopicDto[]) => {
    const pickedIds = pickedList.map((topic) => topic.topicId);
    setQueryState("topicIds", queryStateAnyToStringConverter(pickedIds));
  };

  const popPicker = () => {
    dispatch(
      popTopicPickerModal({ visible: true, team, workspace, multiple: true, initialSelectionOnMultiple: selectedTopics, onPick })
    );
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
