import {selectAuthState} from "@/slice/accountSlice.ts";
import {useTypedSelector} from "@/store";
import {useRouteIfLoggedIn} from "@/hooks/useRouteIfLoggedIn.ts";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {useMeQuery} from "@/api/accountApi.ts";

export default function AppPage() {
    const navigate = useNavigate();
    const authState = useTypedSelector(selectAuthState);
    const routeIfLoggedIn = useRouteIfLoggedIn();
    const {data, error, isLoading} = useMeQuery();

    // useEffect(() => {
    //     if (authState == "NOT_LOGGED_IN") {
    //         navigate("/login", {replace: true});
    //     } else if (authState == "LOGGED_IN") {
    //         navigate(routeIfLoggedIn, {replace: true});
    //     }
    //
    // }, [authState, routeIfLoggedIn, navigate]);

    return <div>
        <p style={{lineBreak:'anywhere'}}>{JSON.stringify(data)}</p>
    </div>;
}
