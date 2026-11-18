import React, {useEffect, useRef, useState} from "react";
import Modal from "@/components/modal/modal/Modal";
import styles from "./UploadStatusModal.module.scss";
import Button, {ButtonVariants} from "@/components/button";
import {
    closeUploadStatusModal,
    removeFromUploadStatusModalQueue,
    selectUploadStatusModalMinimized,
    selectUploadStatusModalPresignedUploadData,
    selectUploadStatusModalVisible,
    setUploadStatusModalItemStatus,
    setUploadStatusModalMouseOver,
    toggleUploadStatusModalMinimized
} from "@/slice/modalSlice";
import type {IUploadQueueItem, UploadStatus} from "@/model/app/store/modal/modalState";
import {useAppDispatch, useTypedSelector} from "@/store";
import {LuChevronDown, LuFolder, LuX} from "react-icons/lu";
import {getMaterialIcon} from "@/components/workspaceFilesPage/materialListView/materialViewRow/MaterialViewRow";
import useTranslation from "@/locals/useTranslation";
import cn from "classnames";

interface ActiveUpload {
    uploadId: string;
    xhr: XMLHttpRequest;
}

const EMPTY_UPLOAD_QUEUE: IUploadQueueItem[] = [];
const FULL_PERCENT = 100;

const isSuccessStatus = (httpStatus: number) => httpStatus >= 200 && httpStatus < 300;

const sendFile = (
    uploadData: IUploadQueueItem,
    onProgress: (percent: number) => void,
    onFinish: (status: UploadStatus) => void
): XMLHttpRequest => {
    const xhr = new XMLHttpRequest();
    xhr.upload.onprogress = (event) => {
        if (event.lengthComputable) {
            onProgress(Math.round((event.loaded / event.total) * FULL_PERCENT));
        }
    };
    xhr.onload = () => onFinish(isSuccessStatus(xhr.status) ? "DONE" : "FAILED");
    xhr.onerror = () => onFinish("FAILED");
    xhr.open("PUT", uploadData.presignedUrl);
    xhr.setRequestHeader("Content-Type", uploadData.file.type);
    xhr.send(uploadData.file);
    return xhr;
};

const UploadStatusModal: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectUploadStatusModalVisible);
    const uploadQueue = useTypedSelector(selectUploadStatusModalPresignedUploadData) ?? EMPTY_UPLOAD_QUEUE;
    const minimized = useTypedSelector(selectUploadStatusModalMinimized);
    const activeUploadRef = useRef<ActiveUpload | null>(null);
    const [activeUploadId, setActiveUploadId] = useState<string | null>(null);
    const [activeUploadPercent, setActiveUploadPercent] = useState<number>(0);
    const anyWaitingUploadExists = uploadQueue.some((item) => item.status == "QUEUED");

    useEffect(() => {
        if (activeUploadRef.current) {
            return;
        }
        const nextUpload = uploadQueue.find((item) => item.status == "QUEUED");
        if (!nextUpload) {
            return;
        }
        const uploadId = nextUpload.relatedObjectId;
        const finishUpload = (status: UploadStatus) => {
            dispatch(setUploadStatusModalItemStatus({relatedObjectId: uploadId, status}));
            activeUploadRef.current = null;
            setActiveUploadId(null);
            if (status == "DONE") {
                nextUpload.onComplete?.();
            }
        };
        const xhr = sendFile(nextUpload, setActiveUploadPercent, finishUpload);
        activeUploadRef.current = {uploadId, xhr};
        setActiveUploadId(uploadId);
        setActiveUploadPercent(0);
    }, [uploadQueue, activeUploadId, dispatch]);

    useEffect(() => {
        return () => {
            activeUploadRef.current?.xhr.abort();
            activeUploadRef.current = null;
        };
    }, []);

    const abortActiveUpload = () => {
        activeUploadRef.current?.xhr.abort();
        activeUploadRef.current = null;
        setActiveUploadId(null);
    };

    const cancelAllUploads = () => {
        dispatch(closeUploadStatusModal());
        abortActiveUpload();
    };

    const cancelSingleUpload = (uploadId: string) => {
        const isActiveUpload = activeUploadRef.current?.uploadId == uploadId;
        dispatch(removeFromUploadStatusModalQueue(uploadId));
        if (isActiveUpload) {
            abortActiveUpload();
        }
    };

    const retrieveItemPercent = (item: IUploadQueueItem) => {
        if (item.status != "QUEUED") {
            return FULL_PERCENT;
        }
        return item.relatedObjectId == activeUploadId ? activeUploadPercent : 0;
    };

    const toggleMinimized = () => {
        dispatch(toggleUploadStatusModalMinimized());
    };

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
            closeButtonIcon={<LuChevronDown className={cn("icon", minimized && styles.chevronIconUp)}/>}
        >
            {!minimized &&
                <>
                    <div
                        className={styles.uploadItemList}
                        onMouseEnter={onMouseEnter}
                        onMouseLeave={onMouseLeave}
                    >
                        {uploadQueue.map((item) => {
                            const Icon = getMaterialIcon(item.file.type) ?? LuFolder;
                            const percent = retrieveItemPercent(item);
                            const isFailed = item.status == "FAILED";
                            return (
                                <div key={item.relatedObjectId} className={styles.uploadItemContainer}>
                                    <div className={styles.uploadItemNameContainer}>
                                        <Icon className={"icon"}/>
                                        <span className={"flex-1"}>{item.file.name}</span>
                                        <Button onClick={() => cancelSingleUpload(item.relatedObjectId)}>
                                            <LuX className={"icon"}/>
                                        </Button>
                                    </div>
                                    <div className={styles.progressContainer}>
                                        <div
                                            style={{flex: percent}}
                                            className={cn(styles.progressFilled, isFailed && styles.progressFailed)}/>
                                        <div style={{flex: FULL_PERCENT - percent}}/>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                    <Button onClick={cancelAllUploads} variant={ButtonVariants.outline}>
                        {anyWaitingUploadExists ? t("uploadStatusModalCancelAll") : t("uploadStatusModalClose")}
                    </Button>
                </>}
        </Modal>
    );
};

export default UploadStatusModal;
