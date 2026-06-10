"use client";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";
import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { selectDeviceOfflineModal } from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { IoCloudOfflineOutline } from "react-icons/io5";
import { GIF_LOADING_ACCOUNT_IDS } from "../loadingModal/LoadingModal";
import Modal from "../modal/Modal";
import styles from "./DeviceOfflineModal.module.css";

const logger = Logger("DeviceOfflineModal");

interface DeviceOfflineModalProps {}

interface ImageState {
  x: number;
  y: number;
  xSpeed: number;
  ySpeed: number;
  filter?: string;
}

const MS_PER_FRAME = 5;
const getRandomNumber = (min: number, max: number): number => {
  return Math.random() * (max - min) + min;
};
const LOGO_WIDTH = 84;
const LOGO_HEIGHT = 125;

const DeviceOfflineModal: React.FC<DeviceOfflineModalProps> = ({}) => {
  const { t } = useTranslation();
  const visible = useTypedSelector(selectDeviceOfflineModal);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const networkEasterEggEnabled = useFeatureFlag("EASTER_EGG_NETWORK");
  const interval = useRef<any>();

  const { width, height } = useWindowSize();
  const [imgState, setImgState] = useState<ImageState>({
    x: getRandomNumber(0, width - LOGO_WIDTH || 0),
    y: getRandomNumber(0, height - LOGO_HEIGHT || 0),
    xSpeed: 1,
    ySpeed: 1,
  });

  useEffect(() => {
    setImgState({
      x: getRandomNumber(0, width - LOGO_WIDTH || 0),
      y: getRandomNumber(0, height - LOGO_HEIGHT || 0),
      xSpeed: 1,
      ySpeed: 1,
    });
  }, [width, height]);

  const gifBounce = useMemo(() => {
    const should = __DEV__ || networkEasterEggEnabled || GIF_LOADING_ACCOUNT_IDS.indexOf(currentAccountId || "") != -1;
    logger.log({ should, indexOf: GIF_LOADING_ACCOUNT_IDS.indexOf(currentAccountId || "") });
    return should;
  }, [currentAccountId, networkEasterEggEnabled]);

  useEffect(() => {
    if (gifBounce && visible && interval.current == null) {
      interval.current = setInterval(() => moveDVDLogo(), MS_PER_FRAME);
      return () => {
        clearInterval(interval.current);
        interval.current = null;
      };
    }
  }, [gifBounce, visible, imgState, setImgState]);

  const moveDVDLogo = () => {
    const filter = [`sepia(100%)`, `saturate(8)`, `contrast(200%) brightness(150%)`, `unset`];
    let next = { ...imgState, x: imgState.x + imgState.xSpeed, y: imgState.y + imgState.ySpeed };
    const wLimit = next.x + LOGO_WIDTH;
    const hLimit = next.y + LOGO_HEIGHT;
    if (wLimit >= width || next.x <= 0) {
      next.xSpeed = -next.xSpeed;
      next.filter = filter[parseInt(`${(Math.random() * 100) % filter.length}`)];
    }
    if (hLimit >= height || next.y <= 0) {
      next.ySpeed = -next.ySpeed;
      next.filter = filter[parseInt(`${(Math.random() * 100) % filter.length}`)];
    }
    logger.log({ moveDVDLogo: next });
    setImgState(next);
  };

  return (
    <Modal
      visible={visible}
      hasTitleCloseButton={false}
      bodyClass={styles.body}
      contentContainerClass={styles.contentContainerClass}
    >
      <IoCloudOfflineOutline size={36} />
      <div className="spacer-h-4" />
      <h2>{t("deviceOfflineModalText")}</h2>
      {gifBounce && (
        <div className={styles.screenSaverContainer}>
          <div
            className={styles.image}
            style={{ transform: `translate(${imgState.x}px, ${imgState.y}px)`, filter: `${imgState.filter}` }}
          ></div>
        </div>
      )}
    </Modal>
  );
};

export default DeviceOfflineModal;
