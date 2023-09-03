import { useRetrieveSubscriptionInfoQuery } from "@/store/api/paymentInfoApi";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import Button from "../button";
import CircularLoading from "../circularLoading/CircularLoading";
import styles from "./WorkspaceSubscriptionInfo.module.css";
import PaymentInfoDetail from "./paymentInfoDetail/PaymentInfoDetail";

interface WorkspaceSubscriptionInfoProps {
  workspaceId: string;
}

const WorkspaceSubscriptionInfo: React.FC<WorkspaceSubscriptionInfoProps> = ({ workspaceId }) => {
  const { t } = useTranslation();
  const { data: retrieveSubscriptionInfoResponse, isFetching, isError } = useRetrieveSubscriptionInfoQuery({ workspaceId });

  const cancelsAfter = useMemo(() => {
    try {
      const cancelsAfter = retrieveSubscriptionInfoResponse?.data.cancelsAfter;
      if (cancelsAfter) {
        return format(new Date(cancelsAfter), t("dateFormat"));
      }
    } catch (error) {
      console.error(error);
    }
    return "";
  }, [retrieveSubscriptionInfoResponse]);

  const nextBillingDate = useMemo(() => {
    try {
      const latestPayment = retrieveSubscriptionInfoResponse?.data.subscriptionPaymentInfoList?.[0];
      const parsedNextBillDate = latestPayment?.parsedNextBillDate;
      if (parsedNextBillDate) {
        return format(new Date(parsedNextBillDate), t("dateFormat"));
      }
    } catch (error) {
      console.error(error);
    }
    return "";
  }, [retrieveSubscriptionInfoResponse]);

  return (
    <div className={styles.container}>
      {isFetching ? (
        <CircularLoading />
      ) : isError ? (
        <>{t("genericError")}</>
      ) : (
        <>
          <h2>{t("billingTitle")}</h2>
          <div className={styles.subscriptionDetail}>
            {cancelsAfter ? (
              <>{t("subscriptionCancelsAfter")?.replace("${date}", cancelsAfter)}</>
            ) : (
              <>
                <div className={styles.nextBillingDate}>
                  {t("subscriptionNextBillingDate").replace("${date}", nextBillingDate)}
                </div>
                <div className={styles.subscriptionActionButtonsContainer}>
                  <Button href={retrieveSubscriptionInfoResponse?.data.retrieveSubscriptionEditInfo.updateUrl} target="_blank">
                    {t("subscriptionUpdate")}
                  </Button>
                  <Button href={retrieveSubscriptionInfoResponse?.data.retrieveSubscriptionEditInfo.cancelUrl} target="_blank">
                    {t("subscriptionCancel")}
                  </Button>
                </div>
              </>
            )}
          </div>

          <div className={styles.receiptList}>
            {retrieveSubscriptionInfoResponse?.data?.subscriptionPaymentInfoList?.map((subscriptionPaymentInfo) => (
              <PaymentInfoDetail
                key={`${subscriptionPaymentInfo.relatedEntityId}`}
                subscriptionPaymentInfo={subscriptionPaymentInfo}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default WorkspaceSubscriptionInfo;
