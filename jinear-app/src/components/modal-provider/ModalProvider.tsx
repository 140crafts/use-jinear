import ErrorBoundary from "@/components/error-boundary/ErrorBoundary";
import React from "react";
import BasicTextInputModal from "@/components/modal/basicTextInputModal/BasicTextInputModal";
import DatePickerModal from "@/components/modal/datePicker/DatePickerModal";
import DialogModal from "@/components/modal/dialogModal/DialogModal";
import LoadingModal from "@/components/modal/loadingModal/LoadingModal";
import LoginWith2FaMailModal from "@/components/modal/loginWith2FaMailModal/LoginWith2FaMailModal";
import NotFoundModal from "@/components/modal/notFoundModal/NotFoundModal";
import DeviceOfflineModal from "../modal/deviceOfflineModal/DeviceOfflineModal";


import InstallPwaInstructionsModal from "@/components/modal/installPwaInstructionsModal/InstallPwaInstructionsModal";

interface ModalProviderProps {
}

const globalModals: any = (
    <>
        <LoginWith2FaMailModal/>
        <InstallPwaInstructionsModal/>
        <DatePickerModal/>
        <BasicTextInputModal/>
        <DialogModal/>
        <NotFoundModal/>
        <DeviceOfflineModal/>
        <LoadingModal/>
    </>
);

const ModalProvider: React.FC<ModalProviderProps> = ({}) => {
    return (
        <ErrorBoundary message={"An unexpected error occurred while rendering modals."}>
            {globalModals}
        </ErrorBoundary>);
};

export default ModalProvider;
