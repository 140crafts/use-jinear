import React, { ChangeEvent, useEffect, useMemo, useRef, useState } from "react";
import styles from "./GenericSelectModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import useTranslation from "@/locals/useTranslation";
import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { levDist } from "@/utils/lev-dist";
import Logger from "@/utils/logger";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { IoClose } from "react-icons/io5";

export interface IGenericPickerModalElement {
  id: string,
  label: string,
  data: any,
  Icon?: React.ReactNode,
  __levDist?: number
}

interface GenericSelectModalProps {
  visible?: boolean;
  title: string;
  multiple?: boolean;
  modalData?: IGenericPickerModalElement[],
  initialSelectionOnMultiple?: IGenericPickerModalElement[];
  onPick?: (pickedList: IGenericPickerModalElement[]) => void;
  searchable?: boolean
  searchInputPlaceholder?: string,
  belowSearchButtonLabel?: string,
  onBelowSearchButtonClick?: () => void,
  emptyLabel?: string
  emptyButtonLabel?: string
  onEmptyButtonClick?: () => void,
  requestClose?: () => void;
}

const logger = Logger("GenericSelectModal");

const GenericSelectModal: React.FC<GenericSelectModalProps> = ({
                                                                 visible = false,
                                                                 title,
                                                                 multiple,
                                                                 modalData,
                                                                 initialSelectionOnMultiple,
                                                                 onPick,
                                                                 searchable,
                                                                 searchInputPlaceholder,
                                                                 belowSearchButtonLabel,
                                                                 onBelowSearchButtonClick,
                                                                 emptyLabel,
                                                                 emptyButtonLabel,
                                                                 onEmptyButtonClick,
                                                                 requestClose
                                                               }) => {
  const { t } = useTranslation();

  const [input, setInput] = useState<string>("");
  const [searchValue, setSearchValue] = useState<string>("");
  const searchInputRef = useRef<HTMLInputElement>(null);
  const [selectedItems, setSelectedItems] = useState<IGenericPickerModalElement[]>([]);

  useDebouncedEffect(() => setSearchValue(input), [input], 750);

  useEffect(() => {
    setSelectedItems(initialSelectionOnMultiple || []);
  }, [JSON.stringify(initialSelectionOnMultiple)]);

  useEffect(() => {
    setTimeout(() => {
      if (visible && searchInputRef.current) {
        searchInputRef?.current?.focus?.();
      }
    }, 250);
  }, [visible]);

  const sortedAndUnselectedData = useMemo(() => {
    if (searchValue?.length == 0) {
      return modalData?.filter(el => !selectedItems?.some(sel => sel.id == el.id));
    }
    return modalData
      ?.filter(el => !selectedItems?.some(sel => sel.id == el.id))
      ?.map(el => {
        return { ...el, __levDist: levDist(searchValue, el?.label || "") };
      })
      ?.sort((a, b) => (a.__levDist == null ? 0 : a.__levDist) - (b.__levDist == null ? 0 : b.__levDist));
  }, [searchValue, modalData, selectedItems]);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setInput(e.target.value);
  };

  const addToSelected = (el: IGenericPickerModalElement) => {
    if (!selectedItems?.some(sel => sel.id == el.id)) {
      setSelectedItems([...selectedItems, el]);
    }
  };

  const pickAndClose = (el: IGenericPickerModalElement) => {
    onPick?.([el]);
    close?.();
  };

  const submitPickedAndClose = () => {
    onPick?.(selectedItems);
    close?.();
  };

  const removeFromSelected = (toRemove: IGenericPickerModalElement) => {
    const filtered = selectedItems.filter((el) => el.id != toRemove.id);
    setSelectedItems(filtered);
  };

  const close = () => {
    setInput("");
    setSearchValue("");
    setSelectedItems([]);
    requestClose?.();
  };

  logger.log({ sortedAndUnselectedData, selectedItems, modalData });

  return (
    <Modal
      visible={visible}
      title={title}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
      height={"height-medium-or-full"}
    >
      {searchable &&
        <div className={styles.inputContainer}>
          <input
            ref={searchInputRef}
            type={"text"}
            className={styles.searchInput}
            placeholder={searchInputPlaceholder}
            onChange={onTextChange}
          />
        </div>
      }

      {belowSearchButtonLabel && onBelowSearchButtonClick && (
        <div className={styles.belowSearchButtonContainer}>
          <Button variant={ButtonVariants.outline}
                  onClick={onBelowSearchButtonClick}
                  heightVariant={ButtonHeight.mid}
                  className={"flex-1"}
          >
            {belowSearchButtonLabel}
          </Button>
        </div>
      )}

      {(modalData == null || modalData.length == 0) && (
        <div className={styles.emptyInfoContainer}>
          <span>{emptyLabel ? emptyLabel : t("genericPickerModalListEmptyLabel")}</span>
          {emptyButtonLabel && onEmptyButtonClick && (
            <Button onClick={onEmptyButtonClick}
                    variant={ButtonVariants.contrast}
                    heightVariant={ButtonHeight.short}
            >
              {emptyButtonLabel}
            </Button>
          )}
        </div>
      )}

      {sortedAndUnselectedData && sortedAndUnselectedData?.length != 0 && (
        <div className={styles.list}>
          {sortedAndUnselectedData?.map((el, i) => (
            <Button key={`generic-picker-el-${el.id}-${i}`}
                    className={styles.listItemButton}
                    onClick={() => {
                      multiple ? addToSelected(el) : pickAndClose(el);
                    }}
            >
              {el.Icon}
              {el.label}
            </Button>
          ))}
        </div>
      )}

      {multiple && selectedItems.length != 0 && (
        <div className={styles.selectedItemListContainer}>
          {selectedItems.map((el, i) => (
            <div key={`selected-el-${el.id}-${i}`} className={styles.selectedItemContainer}>
              <div className={styles.selectedItemLabel}>
                {el.Icon}
                {el.label}
              </div>
              <Button
                variant={ButtonVariants.filled2}
                className={styles.selectedItemUnselectButton}
                onClick={() => removeFromSelected(el)}
              >
                <IoClose />
              </Button>
            </div>
          ))}
        </div>
      )}

      {multiple && (
        <div className={styles.actionBar}>
          <Button heightVariant={ButtonHeight.short} onClick={close}>
            {t("genericPickerModalCancelButton")}
          </Button>
          <Button
            heightVariant={ButtonHeight.short}
            variant={ButtonVariants.contrast}
            className={styles.contButton}
            onClick={submitPickedAndClose}
            // disabled={selectedItems?.length == 0}
          >
            {t("genericPickerModalSelectButton")}
          </Button>
        </div>
      )}

    </Modal>
  );
};

export default GenericSelectModal;