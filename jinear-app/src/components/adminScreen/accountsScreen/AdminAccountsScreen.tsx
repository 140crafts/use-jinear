import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import Pagination from "@/components/pagination/Pagination";
import {useAdminRetrieveAccountsQuery} from "@/store/api/adminAccountApi";
import {popAdminCreateAccountModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useState} from "react";
import AdminAccountRow from "./accountRow/AdminAccountRow";
import styles from "./AdminAccountsScreen.module.css";

const AdminAccountsScreen: React.FC = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const [page, setPage] = useState<number>(0);

    const {data: response, isLoading, isFetching} = useAdminRetrieveAccountsQuery({page});

    const popCreateModal = () => {
        dispatch(popAdminCreateAccountModal({visible: true}));
    };

    return (
        <div className={styles.container}>
            <div className={styles.titleBar}>
                <h2>{t("adminAccountsTitle")}</h2>
                <Button variant={ButtonVariants.contrast} onClick={popCreateModal}>
                    {t("adminAccountsCreateButtonLabel")}
                </Button>
            </div>

            <div className={styles.header}>
                {response && (
                    <Pagination
                        id={"admin-account-list-paginator"}
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
                                <th className={styles.emailHeader}>{t("adminTableHeaderEmail")}</th>
                                <th>{t("adminTableHeaderCreatedDate")}</th>
                                <th className={styles.actionsHeader}>{t("adminTableHeaderActions")}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {response.data.content.map((account) => (
                                <AdminAccountRow
                                    key={`admin-account-row-${account.accountId}`}
                                    account={account}
                                />
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
                {!response?.data.hasContent && !isFetching && (
                    <div className={styles.emptyStateContainer}>
                        <div className={styles.emptyLabel}>{t("adminAccountsEmptyLabel")}</div>
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

export default AdminAccountsScreen;
