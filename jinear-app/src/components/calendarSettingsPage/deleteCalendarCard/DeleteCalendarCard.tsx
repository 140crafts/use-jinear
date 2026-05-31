import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import {useDeleteCalendarMutation} from "@/store/api/calendarApi";
import {changeLoadingModalVisibility, closeDialogModal, popDialogModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";
import styles from "./DeleteCalendarCard.module.css";
import {useNavigate} from "react-router-dom";

interface DeleteCalendarCardProps {
    calendarId: string;
}

const DeleteCalendarCard: React.FC<DeleteCalendarCardProps> = ({calendarId}) => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const [deleteCalendar, {isLoading, isSuccess}] = useDeleteCalendarMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isLoading}));
        if (isSuccess) {
            navigate(ROUTE_IF_LOGGED_IN, {replace: true});
        }
    }, [isLoading, isSuccess]);

    const popAreYouSure = () => {
        dispatch(
            popDialogModal({
                visible: true,
                title: t("deleteCalendarCardAreYouSureTitle"),
                content: t("deleteCalendarCardAreYouSureText"),
                confirmButtonLabel: t("deleteCalendarButton"),
                onConfirm: deleteThisCalendar,
            })
        );
    };

    const deleteThisCalendar = () => {
        deleteCalendar({calendarId});
        dispatch(closeDialogModal());
    };

    return (
        <div className={styles.container}>
            <SectionTitle title={t("deleteCalendarCardTitle")} description={t("deleteCalendarCardText")}/>
            <div className={styles.actionContainer}>
                <Button
                    onClick={popAreYouSure}
                    variant={ButtonVariants.filled}
                    heightVariant={ButtonHeight.short}
                    disabled={isLoading}
                    loading={isLoading}
                >
                    {t("deleteCalendarButton")}
                </Button>
            </div>
        </div>
    );
};

export default DeleteCalendarCard;
