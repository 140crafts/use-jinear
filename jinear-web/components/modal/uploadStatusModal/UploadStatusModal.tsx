import React, { useEffect, useRef, useState } from "react";
import Modal from "@/components/modal/modal/Modal";
import styles from "./UploadStatusModal.module.scss";
import Button, { ButtonVariants } from "@/components/button";
import { IoChevronDown } from "react-icons/io5";
import {
  closeUploadStatusModal,
  removeFromUploadStatusModalQueue,
  resetUploadStatusModalQueue,
  selectUploadStatusModalMinimized,
  selectUploadStatusModalPresignedUploadData,
  selectUploadStatusModalVisible,
  selectUploadStatusModalWorkspaceId, setUploadStatusModalMouseOver, toggleUploadStatusModalMinimized
} from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { LuChevronDown, LuFolder, LuX } from "react-icons/lu";
import { getMaterialIcon } from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import useTranslation from "@/locals/useTranslation";
import cn from "classnames";
import Logger from "@/utils/logger";

interface UploadStatusModalProps {

}

const logger = Logger("UploadStatusModal");

const UploadStatusModal: React.FC<UploadStatusModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectUploadStatusModalVisible);
  const presignedUploadData = useTypedSelector(selectUploadStatusModalPresignedUploadData) ?? [];
  const workspaceId = useTypedSelector(selectUploadStatusModalWorkspaceId);
  const [uploadProgresses, setUploadProgresses] = useState<{ [key: string]: number }>({});
  const [currentUploadId, setCurrentUploadId] = useState<string | null>(null);
  const minimized = useTypedSelector(selectUploadStatusModalMinimized);
  const isProcessingRef = useRef(false);
  const abortControllerRef = useRef<AbortController | null>(null);
  const xhrRef = useRef<XMLHttpRequest | null>(null);
  const anyOngoingUploadExists = Object.values(uploadProgresses).some(value => [100, -1].indexOf(value) == -1);

  logger.log({ uploadProgresses });

  const uploadWithProgress = (
    url: string,
    file: File,
    onProgress: (percent: number) => void,
    abortSignal: AbortSignal
  ): Promise<void> => {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      xhrRef.current = xhr;

      abortSignal.addEventListener("abort", () => {
        xhr.abort();
        reject(new Error("Upload cancelled"));
      });

      xhr.upload.onprogress = (event) => {
        if (event.lengthComputable) {
          const percent = Math.round((event.loaded / event.total) * 100);
          onProgress(percent);
        }
      };
      xhr.onload = () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          resolve();
        } else {
          reject(new Error("File upload failed."));
        }
      };
      xhr.onerror = () => reject(new Error("File upload failed."));
      xhr.onabort = () => reject(new Error("Upload cancelled"));
      xhr.open("PUT", url);
      xhr.setRequestHeader("Content-Type", file.type);
      xhr.send(file);
    });
  };

  const cancelAllUploads = () => {
    if (abortControllerRef.current) {
      abortControllerRef.current.abort();
      abortControllerRef.current = null;
    }
    isProcessingRef.current = false;
    setCurrentUploadId(null);
    setUploadProgresses({});

    dispatch(resetUploadStatusModalQueue());
    dispatch(closeUploadStatusModal());
    dispatch(toggleUploadStatusModalMinimized());
  };

  const cancelSingleUpload = (uploadId: string) => {
    if (currentUploadId === uploadId && xhrRef.current) {
      xhrRef.current.abort();
    }
    setUploadProgresses((prev) => {
      const updated = { ...prev };
      delete updated[uploadId];
      return updated;
    });
    dispatch(removeFromUploadStatusModalQueue(uploadId));
  };

  const toggleMinimized = () => {
    dispatch(toggleUploadStatusModalMinimized());
  };

  useEffect(() => {
    if (visible && presignedUploadData?.length === 0 && !currentUploadId && !isProcessingRef.current) {
      dispatch(closeUploadStatusModal());
    }
  }, [visible, presignedUploadData, currentUploadId, dispatch]);

  useEffect(() => {
    return () => {
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
    };
  }, []);

  useEffect(() => {
    const processQueue = async () => {
      if (!visible || !presignedUploadData || presignedUploadData.length === 0) {
        return;
      }
      if (isProcessingRef.current) {
        return;
      }

      isProcessingRef.current = true;
      abortControllerRef.current = new AbortController();

      for (const uploadData of presignedUploadData) {
        if (abortControllerRef.current?.signal.aborted) {
          break;
        }

        const uploadId = uploadData.relatedObjectId;
        setCurrentUploadId(uploadId);

        try {
          await uploadWithProgress(
            uploadData.presignedUrl,
            uploadData.file,
            (percent) => {
              setUploadProgresses((prev) => ({
                ...prev,
                [uploadId]: percent
              }));
            },
            abortControllerRef.current.signal
          );

          setUploadProgresses((prev) => ({
            ...prev,
            [uploadId]: 100
          }));

          uploadData?.onComplete?.();

        } catch (error) {
          console.error(`Upload failed for ${uploadId}:`, error);
          setUploadProgresses((prev) => ({
            ...prev,
            [uploadId]: -1
          }));
        }
      }
      setCurrentUploadId(null);
      isProcessingRef.current = false;
    };
    processQueue();
  }, [visible, presignedUploadData, dispatch]);

  const onMouseEnter = () => {
    dispatch(setUploadStatusModalMouseOver(true));
  };

  const onMouseLeave = () => {
    dispatch(setUploadStatusModalMouseOver(false));
  };

  return (
    <Modal
      visible={visible}
      title={t("uploadStatusModalTitle")}
      contentContainerClass={styles.modalContentContainer}
      contentClassName={styles.modalContent}
      containerClassName={styles.modalContainer}
      closepadClassName={minimized ? styles.closepadWithoutBackdropClassName : styles.closepadWithBackdropClassName}
      bodyClass={cn(styles.body, minimized ? styles.minimizedBodyClass : undefined)}
      hasTitleCloseButton={true}
      requestClose={toggleMinimized}
      closeButtonIcon={<LuChevronDown className={cn("icon", minimized && styles.chevronIconUp)} />}
    >
      {!minimized &&
        <>
          <div
            className={styles.uploadItemList}
            onMouseEnter={onMouseEnter}
            onMouseLeave={onMouseLeave}
          >
            {presignedUploadData?.map((item) => {
              const Icon = getMaterialIcon(item.file.type) ?? LuFolder;
              return (
                <div key={item.relatedObjectId} className={styles.uploadItemContainer}>
                  <div className={styles.uploadItemNameContainer}>
                    <Icon className={"icon"} />
                    <span className={"flex-1"}>{item.file.name}</span>
                    <Button onClick={() => cancelSingleUpload(item.relatedObjectId)}>
                      <LuX className={"icon"} />
                    </Button>
                  </div>
                  <div className={styles.progressContainer}>
                    <div
                      style={{ flex: uploadProgresses[item.relatedObjectId] ?? 0 }}
                      className={styles.progressFilled} />
                    <div
                      style={{ flex: 100 - (uploadProgresses[item.relatedObjectId] ?? 0) }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
          <Button onClick={cancelAllUploads} variant={ButtonVariants.outline}>
            {anyOngoingUploadExists ? t("uploadStatusModalCancelAll") : t("uploadStatusModalClose")}
          </Button>
        </>}
    </Modal>
  );
};

export default UploadStatusModal;


