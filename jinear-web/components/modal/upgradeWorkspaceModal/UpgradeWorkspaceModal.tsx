import Button, { ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import JinearProInfo from "@/components/jinearProInfo/JinearProInfo";
import WorkspaceInfo from "@/components/workspaceInfo/WorkspaceInfo";
import useWindowSize from "@/hooks/useWindowSize";
import { useRefreshPaymentsMutation } from "@/store/api/paymentProcessApi";
import { selectCurrentAccount, selectCurrentAccountsWorkspace } from "@/store/slice/accountSlice";
import {
  closeUpgradeWorkspacePlanModal,
  selectUpgradeWorkspacePlanModalVisible,
  selectUpgradeWorkspacePlanModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import { isWorkspaceInPaidTier } from "@/utils/permissionHelper";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { IoCheckmarkCircle } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./UpgradeWorkspaceModal.module.css";

interface UpgradeWorkspaceModalProps {}

export const PADDLE_CATALOG = {
  business_daily: { sandbox: 63817, prod: -1, price: "3.30$" },
  business_monthly: { sandbox: 63716, prod: 848738, price: "49.90$" },
  business_yearly: { sandbox: 63717, prod: 848737, price: "499$" },
};

const logger = Logger("UpgradeWorkspaceModal");

const UpgradeWorkspaceModal: React.FC<UpgradeWorkspaceModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const router = useRouter();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectUpgradeWorkspacePlanModalVisible);
  const workspaceId = useTypedSelector(selectUpgradeWorkspacePlanModalWorkspaceId);
  const workspace = useTypedSelector(selectCurrentAccountsWorkspace(workspaceId));
  const isInPaidTier = isWorkspaceInPaidTier(workspace);
  const account = useTypedSelector(selectCurrentAccount);
  const [refreshPayments, { isLoading, isSuccess }] = useRefreshPaymentsMutation();

  useEffect(() => {
    if (visible && workspaceId) {
      // @ts-ignore
      global.upgradeWorkspaceSuccess = () => {
        logger.log({ paymentSuccessForWorkspaceId: workspaceId });
        refreshPayments();
      };
    }
  }, [visible, workspaceId, refreshPayments]);

  useEffect(() => {
    logger.log({ isInPaidTier, workspaceId, workspace, isSuccess });
    if (workspace && isSuccess && !isLoading && isInPaidTier != null) {
      if (isInPaidTier) {
        router.replace(`/${workspace?.username}/settings`);
        close();
      } else {
        setTimeout(() => {
          refreshPayments();
        }, 3000);
      }
    }
  }, [workspace, isInPaidTier, workspaceId, isSuccess]);

  const close = () => {
    dispatch(closeUpgradeWorkspacePlanModal());
  };

  const openCheckoutForm = (vo: { productId: number }) => {
    if (account) {
      const { accountId, email, emailConfirmed } = account;
      const passthrough = { accountId, workspaceId, email, emailConfirmed };
      // @ts-ignore
      Paddle.Checkout.open({
        product: vo.productId,
        email: email,
        passthrough: JSON.stringify(passthrough),
        // success: `${HOST}/subscription-processing`,
        successCallback: "upgradeWorkspaceSuccess",
      });
    }
  };
  return (
    <Modal
      visible={visible}
      title={t("upgradeWorkspaceTierModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {isLoading ? (
        <CircularLoading />
      ) : isSuccess && isInPaidTier ? (
        <div className={styles.successContainer}>
          <IoCheckmarkCircle size={48} className={styles.successIcon} />
          <h2>{t("upgradeWorkspaceTierSuccess")}</h2>
        </div>
      ) : (
        <>
          <JinearProInfo />
          <div className="spacer-h-4" />
          {workspace && <WorkspaceInfo workspace={workspace} />}
          <div className={styles.appliesOnlyWorkspaceText}>{t("upgradeWorkspaceTierModalAppliesToWorkspaceText")}</div>
          <div className="spacer-h-4" />

          {/* {__DEV__ && (
            <>
              <Button
                variant={ButtonVariants.default}
                onClick={() => openCheckoutForm({ productId: PADDLE_CATALOG.business_daily[__DEV__ ? "sandbox" : "prod"] })}
              >
                DEVELOPMENT ONLY PLAN DAILY
              </Button>
              <div className="spacer-h-2" />
            </>
          )} */}

          <Button
            variant={ButtonVariants.contrast}
            onClick={() => openCheckoutForm({ productId: PADDLE_CATALOG.business_monthly[__DEV__ ? "sandbox" : "prod"] })}
          >
            {t("upgradeWorkspaceTierButtonMonthly").replace("${price}", PADDLE_CATALOG.business_monthly.price)}
          </Button>
          <div className="spacer-h-2" />
          <Button
            variant={ButtonVariants.outline}
            onClick={() => openCheckoutForm({ productId: PADDLE_CATALOG.business_yearly[__DEV__ ? "sandbox" : "prod"] })}
          >
            {t("upgradeWorkspaceTierButtonYearly").replace("${price}", PADDLE_CATALOG.business_yearly.price)}
          </Button>
          <div className="spacer-h-4" />
          <div className={styles.autoRenewsText}>{t("upgradeWorkspaceTierModalAutoRenewsText")}</div>
        </>
      )}
    </Modal>
  );
};

export default UpgradeWorkspaceModal;
