import ForgotPasswordForm from "@/components/form/forgotPasswordForm/ForgotPasswordForm";
import FormLogo from "@/components/form-logo/FormLogo";
import {selectIsLoggedIn} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import {ROUTE_IF_LOGGED_IN} from "@/util/constants";
import React, {useEffect} from "react";
import styles from "./index.module.scss";
import {useNavigate} from "react-router-dom";

interface ForgotPasswordPageProps {
}

const ForgotPasswordPage: React.FC<ForgotPasswordPageProps> = ({}) => {
    const navigate = useNavigate();
    const isLoggedIn = useTypedSelector(selectIsLoggedIn);

    useEffect(() => {
        if (isLoggedIn) {
            navigate(ROUTE_IF_LOGGED_IN, {replace: true});
        }
    }, [isLoggedIn]);

    return (
        <div className={styles.container}>
            <ForgotPasswordForm className={styles.form}/>
            <FormLogo/>
        </div>
    );
};

export default ForgotPasswordPage;
