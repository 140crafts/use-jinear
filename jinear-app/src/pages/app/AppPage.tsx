import {selectAuthState} from "@/slice/accountSlice.ts";
import {useTypedSelector} from "@/store";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useAccountsPreferredWorkspaceIfLoggedIn} from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn.ts";
import {useMeQuery} from "@/store/api/accountApi";
import {useOnlineStatus} from "@/hooks/useOnlineStatus";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";
import useTranslation from "@/locales/useTranslation";
import styles from "./AppPage.module.css";

export default function AppPage() {
    const navigate = useNavigate();
    const authState = useTypedSelector(selectAuthState);
    const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
    const preferredRedirectRoute = `/${preferredWorkspace?.username}`;

    const {isError, refetch} = useMeQuery();
    const online = useOnlineStatus();
    const {t} = useTranslation();

    useEffect(() => {
        if (authState == "NOT_LOGGED_IN") {
            navigate("/login", {replace: true});
        } else if (authState == "LOGGED_IN") {
            navigate(preferredRedirectRoute, {replace: true});
        }

    }, [authState, preferredRedirectRoute, navigate]);

    if (authState == "NOT_DECIDED") {
        const cannotReachServer = !online || isError;
        return (
            <div className={styles.bootContainer}>
                {cannotReachServer ? (
                    <>
                        <h1 className={styles.bootTitle}>{t("bootOfflineTitle")}</h1>
                        <p className={styles.bootDescription}>{t("bootOfflineDescription")}</p>
                        <button className={styles.retryButton} onClick={() => refetch()}>
                            {t("bootOfflineRetryButton")}
                        </button>
                    </>
                ) : (
                    <CircularLoading size={28}/>
                )}
            </div>
        );
    }

    return null;
}
