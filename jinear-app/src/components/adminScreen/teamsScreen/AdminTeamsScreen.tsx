import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Pagination from "@/components/pagination/Pagination";
import {useAdminRetrieveWorkspacesQuery} from "@/store/api/adminWorkspaceApi";
import {popAdminCreateTeamModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useState} from "react";
import AdminWorkspaceTeamsGroup from "./workspaceTeamsGroup/AdminWorkspaceTeamsGroup";
import styles from "./AdminTeamsScreen.module.css";

const AdminTeamsScreen: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const {data: response, isLoading, isFetching} = useAdminRetrieveWorkspacesQuery({page});

    const popCreateModal = () => {
        dispatch(popAdminCreateTeamModal({visible: true}));
    };

    return (
        <div className={styles.container}>
            <div className={styles.titleBar}>
                <h2>{t("adminTeamsTitle")}</h2>
                <Button variant={ButtonVariants.contrast} onClick={popCreateModal}>
                    {t("adminTeamsCreateButtonLabel")}
                </Button>
            </div>

            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-teams-workspace-paginator"}
                        className={styles.pagination}
                        pageNumber={response.data.number}
                        pageSize={response.data.size}
                        totalPages={response.data.totalPages}
                        totalElements={response.data.totalElements}
                        hasPrevious={response.data.hasPrevious}
                        hasNext={response.data.hasNext}
                        isLoading={isLoading || isFetching}
                        page={page}
                        setPage={setPage}
                    />
                )}
            </div>

            <div className={styles.content}>
                {response?.data.content.map((workspace) => (
                    <AdminWorkspaceTeamsGroup
                        key={`admin-workspace-teams-group-${workspace.workspaceId}`}
                        workspace={workspace}
                    />
                ))}
                {!response?.data.hasContent && !isFetching && (
                    <div className={styles.emptyStateContainer}>
                        <div className={styles.emptyLabel}>{t("adminWorkspacesEmptyLabel")}</div>
                    </div>
                )}
            </div>

            {isFetching && !response && (
                <div className={styles.loading}>
                    <CircularLoading/>
                </div>
            )}
        </div>
    );
};

export default AdminTeamsScreen;
