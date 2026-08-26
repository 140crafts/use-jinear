import ErrorBoundary from "@/components/error-boundary/ErrorBoundary";
import React from "react";
import AdminAccountPickerModal from "@/components/modal/admin/adminAccountPickerModal/AdminAccountPickerModal.tsx";
import AdminWorkspacePickerModal
    from "@/components/modal/admin/adminWorkspacePickerModal/AdminWorkspacePickerModal.tsx";
import AdminCreateWorkspaceModal
    from "@/components/modal/admin/adminCreateWorkspaceModal/AdminCreateWorkspaceModal.tsx";
import AdminWorkspaceMembersModal
    from "@/components/modal/admin/adminWorkspaceMembersModal/AdminWorkspaceMembersModal.tsx";
import AdminWorkspaceTierModal from "@/components/modal/admin/adminWorkspaceTierModal/AdminWorkspaceTierModal.tsx";
import AdminCreateTeamModal from "@/components/modal/admin/adminCreateTeamModal/AdminCreateTeamModal.tsx";
import AdminTeamMembersModal from "@/components/modal/admin/adminTeamMembersModal/AdminTeamMembersModal.tsx";
import AdminCreateAccountModal from "@/components/modal/admin/adminCreateAccountModal/AdminCreateAccountModal.tsx";
import AdminAccountWorkspacesModal
    from "@/components/modal/admin/adminAccountWorkspacesModal/AdminAccountWorkspacesModal.tsx";
import AdminAccountTeamsModal from "@/components/modal/admin/adminAccountTeamsModal/AdminAccountTeamsModal.tsx";

interface AdminModalProviderProps {
}

const adminModals: any = (
    <>
        <AdminCreateWorkspaceModal/>
        <AdminWorkspaceMembersModal/>
        <AdminWorkspaceTierModal/>

        <AdminCreateTeamModal/>
        <AdminTeamMembersModal/>

        <AdminCreateAccountModal/>
        <AdminAccountWorkspacesModal/>
        <AdminAccountTeamsModal/>

        <AdminAccountPickerModal/>
        <AdminWorkspacePickerModal/>
    </>
);

const AdminModalProvider: React.FC<AdminModalProviderProps> = ({}) => {
    return (
        <ErrorBoundary message={"An unexpected error occurred while rendering admin modals."}>
            {adminModals}
        </ErrorBoundary>);
};

export default AdminModalProvider;
