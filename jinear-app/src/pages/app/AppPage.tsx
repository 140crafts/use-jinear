import {selectAuthState} from "@/slice/accountSlice.ts";
import {useTypedSelector} from "@/store";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useAccountsPreferredWorkspaceIfLoggedIn} from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn.ts";

export default function AppPage() {
    const navigate = useNavigate();
    const authState = useTypedSelector(selectAuthState);
    const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
    const preferredRedirectRoute = `/${preferredWorkspace?.username}`;

    useEffect(() => {
        if (authState == "NOT_LOGGED_IN") {
            navigate("/login", {replace: true});
        } else if (authState == "LOGGED_IN") {
            navigate(preferredRedirectRoute, {replace: true});
        }

    }, [authState, preferredRedirectRoute, navigate]);

    return null;
}
