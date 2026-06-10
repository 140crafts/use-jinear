import SelectDeselectButton from "@/components/selectDeselectButton/SelectDeselectButton";
import { IRelatedFeedItemData } from "@/model/app/store/modal/modalState";
import { TaskInitializeRequest } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { UseFormRegister, UseFormSetValue } from "react-hook-form";
import { LuMailCheck } from "react-icons/lu";
import styles from "./RelatedFeedItemButton.module.css";

interface RelatedFeedItemButtonProps {
  initialRelatedFeedItemData?: IRelatedFeedItemData;
  register: UseFormRegister<TaskInitializeRequest>;
  setValue: UseFormSetValue<TaskInitializeRequest>;
}

const RelatedFeedItemButton: React.FC<RelatedFeedItemButtonProps> = ({ initialRelatedFeedItemData, register, setValue }) => {
  const { t } = useTranslation();
  const [selectedFeedItemData, setSelectedFeedItemData] = useState(initialRelatedFeedItemData);

  useEffect(() => {
    setSelectedFeedItemData(initialRelatedFeedItemData);
  }, [initialRelatedFeedItemData]);

  useEffect(() => {
    setValue("feedId", selectedFeedItemData ? selectedFeedItemData.feedId : undefined);
    setValue("feedItemId", selectedFeedItemData ? selectedFeedItemData.feedItemId : undefined);
  }, [selectedFeedItemData]);

  const deselect = () => {
    setSelectedFeedItemData(undefined);
  };

  return !selectedFeedItemData ? null : (
    <div className={styles.container}>
      <SelectDeselectButton
        hasSelection={initialRelatedFeedItemData != null}
        selectedComponent={
          <div className={styles.selectedContainer}>
            <LuMailCheck />
            <div className="line-clamp">{initialRelatedFeedItemData?.itemTitle || t("newTaskFormFeedItemDefaultTitle")}</div>
          </div>
        }
        onUnpickClick={deselect}
      />
      <input type="hidden" value={selectedFeedItemData.feedId} {...register("feedId")} />
      <input type="hidden" value={selectedFeedItemData.feedItemId} {...register("feedItemId")} />
    </div>
  );
};

export default RelatedFeedItemButton;
