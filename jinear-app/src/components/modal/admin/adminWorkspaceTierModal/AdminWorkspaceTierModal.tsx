import GenericSelectModal, {
    type IGenericPickerModalElement
} from "@/components/modal/genericSelectModal/GenericSelectModal";
import type {WorkspaceTier} from "@/model/be/jinear-core";
import {useAdminUpdateWorkspaceTierMutation} from "@/store/api/adminWorkspaceApi";
import {
    changeLoadingModalVisibility,
    closeAdminWorkspaceTierModal,
    selectAdminWorkspaceTierModalVisible,
    selectAdminWorkspaceTierModalWorkspace
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect} from "react";

interface AdminWorkspaceTierModalProps {
}

const AdminWorkspaceTierModal: React.FC<AdminWorkspaceTierModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectAdminWorkspaceTierModalVisible);
    const workspace = useTypedSelector(selectAdminWorkspaceTierModalWorkspace);
    const [adminUpdateWorkspaceTier, {isLoading}] = useAdminUpdateWorkspaceTierMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isLoading}));
    }, [isLoading]);

    const modalData: IGenericPickerModalElement[] = [
        {id: "BASIC", label: t("adminWorkspaceTier_BASIC"), data: "BASIC"},
        {id: "PRO", label: t("adminWorkspaceTier_PRO"), data: "PRO"}
    ];

    const close = () => {
        dispatch(closeAdminWorkspaceTierModal());
    };

    const onPick = (pickedList: IGenericPickerModalElement[]) => {
        const workspaceTier = pickedList?.[0]?.data as WorkspaceTier;
        if (workspace && workspaceTier) {
            adminUpdateWorkspaceTier({workspaceId: workspace.workspaceId, workspaceTier});
        }
    };

    return (
        <GenericSelectModal
            visible={visible}
            title={t("adminWorkspaceTierModalTitle")}
            modalData={modalData}
            onPick={onPick}
            requestClose={close}
        />
    );
};

export default AdminWorkspaceTierModal;
