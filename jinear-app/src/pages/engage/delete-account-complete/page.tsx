import Button from "@/components/button";
import {useFirstRender} from "@/hooks/useFirstRender";
import {useConfirmAccountDeleteMutation} from "@/store/api/accountDeleteApi";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./index.module.css";
import {useSearchParams} from "react-router-dom";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface DeleteAccountCompletePageProps {
}

const DeleteAccountCompletePage: React.FC<DeleteAccountCompletePageProps> = ({}) => {
    const {t} = useTranslation();
    const [params] = useSearchParams();
    const firstRender = useFirstRender();
    const [confirmAccountDelete, {data, isSuccess, isError, isLoading}] = useConfirmAccountDeleteMutation();

    const token = params.get("token");

    useEffect(() => {
        if (token && !isLoading && !data) {
            confirmAccountDelete(token);
        }
    }, [token, firstRender]);

    return (
        <div className={styles.container}>
            {isLoading && <CircularLoading size={28}/>}
            <div className={styles.infoContainer}>
                {isSuccess && !isError && (
                    <>
                        <div className={styles.title}>{t("accountDeleteConfirmPageSuccessTitle")}</div>
                    </>
                )}
                {!isSuccess && isError && (
                    <>
                        <div className={styles.title}>{t("accountDeleteConfirmPageFailureTitle")}</div>
                        <div>{t("accountDeleteConfirmPageFailureText")}</div>
                        <Button href="/home">{t("notFoundModalReturnHomeButtonLabel")}</Button>
                    </>
                )}
            </div>
        </div>
    );
};

export default DeleteAccountCompletePage;
