import Button from "@/components/button";
import { SubscriptionPaymentInfoDto } from "@/model/be/jinear-core";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import styles from "./PaymentInfoDetail.module.css";

interface PaymentInfoDetailProps {
  subscriptionPaymentInfo: SubscriptionPaymentInfoDto;
}

const PaymentInfoDetail: React.FC<PaymentInfoDetailProps> = ({ subscriptionPaymentInfo }) => {
  const { t } = useTranslation();

  const formattedDate = useMemo(() => {
    const eventTime = subscriptionPaymentInfo.parsedEventTime;
    try {
      return format(new Date(eventTime), t("dateFormat"));
    } catch (error) {
      console.error(error);
    }
    return "";
  }, [subscriptionPaymentInfo]);

  return (
    <div className={styles.container}>
      <div className={styles.date}>{formattedDate}</div>
      <div className="spacer-w-2" />
      <div className={styles.priceInfo}>{`${subscriptionPaymentInfo.balanceGross} ${subscriptionPaymentInfo.currency}`}</div>
      <div className="flex-1" />
      <Button href={subscriptionPaymentInfo.receiptUrl} target="_blank">
        {t("subscriptionReceiptUrlButtonLabel")}
      </Button>
    </div>
  );
};

export default PaymentInfoDetail;
