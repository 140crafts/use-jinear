import type {LocaleType} from "@/model/be/jinear-core";
import {useLoginWithPasswordMutation} from "@/store/api/authApi.ts";
import {useAppDispatch} from "@/store";
import {setAuthStateAsNotDecided, setAuthStateAsNotLoggedIn} from "@/slice/accountSlice";
import {popConfirmEmailCodeModal} from "@/slice/modalSlice.ts";
import {EMAIL_NOT_CONFIRMED_ERROR_CODE, ROUTE_IF_LOGGED_IN} from "@/util/constants.ts";
import useTranslation from "@/locales/useTranslation.ts";
import {format} from "date-fns";
import toast from "react-hot-toast";
import {useNavigate} from "react-router-dom";

const isEmailNotConfirmedError = (error: unknown) =>
    (error as { data?: { errorCode?: string } } | undefined)?.data?.errorCode == EMAIL_NOT_CONFIRMED_ERROR_CODE;

export const usePasswordLogin = () => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [loginWithPassword, {isLoading}] = useLoginWithPasswordMutation();

    const login = async (email: string, password: string) => {
        dispatch(setAuthStateAsNotDecided());
        try {
            await loginWithPassword({
                email,
                password,
                locale: t("localeType") as LocaleType,
                timeZone: format(new Date(), "OOOO")
            }).unwrap();
            toast(t("loginSuccessToast"));
            navigate(ROUTE_IF_LOGGED_IN, {replace: true});
        } catch (error) {
            dispatch(setAuthStateAsNotLoggedIn());
            if (isEmailNotConfirmedError(error)) {
                dispatch(popConfirmEmailCodeModal({
                    visible: true,
                    email,
                    onConfirmed: () => login(email, password)
                }));
            }
        }
    };

    return {login, isLoading};
};
