import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Pagination from "@/components/pagination/Pagination";
import {useAdminRetrieveWorkspacesQuery} from "@/store/api/adminWorkspaceApi";
import {popAdminCreateWorkspaceModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useState} from "react";
import AdminWorkspaceRow from "./workspaceRow/AdminWorkspaceRow";
import styles from "./AdminWorkspacesScreen.module.css";

const AdminWorkspacesScreen: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const {data: response, isLoading, isFetching} = useAdminRetrieveWorkspacesQuery({page});

    const popCreateModal = () => {
        dispatch(popAdminCreateWorkspaceModal({visible: true}));
    };

    return (
        <div className={styles.container}>
            <div className={styles.titleBar}>
                <h2>{t("adminWorkspacesTitle")}</h2>
                <Button variant={ButtonVariants.contrast} onClick={popCreateModal}>
                    {t("adminWorkspacesCreateButtonLabel")}
                </Button>
            </div>

            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-workspace-list-paginator"}
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
                {response?.data.hasContent && (
                    <div className={styles.tableWrapper}>
                        <table className={styles.table}>
                            <thead>
                            <tr>
                                <th className={styles.titleHeader}>{t("adminTableHeaderTitle")}</th>
                                <th>{t("adminTableHeaderHandle")}</th>
                                <th>{t("adminTableHeaderTier")}</th>
                                <th className={styles.actionsHeader}>{t("adminTableHeaderActions")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {response.data.content.map((workspace) => (
                                <AdminWorkspaceRow
                                    key={`admin-workspace-row-${workspace.workspaceId}`}
                                    workspace={workspace}
                                />
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
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

export default AdminWorkspacesScreen;
