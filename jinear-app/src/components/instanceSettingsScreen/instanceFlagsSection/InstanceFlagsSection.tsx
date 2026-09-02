import type {InstanceFlagType} from "@/model/be/jinear-core";
import {useRetrieveInstanceFlagsQuery} from "@/store/api/instanceFlagApi";
import {useSetInstanceFlagMutation} from "@/store/api/adminInstanceFlagOperationApi";
import {isInstanceFlagEnabled} from "@/hooks/useInstanceFlag";
import {changeLoadingModalVisibility} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation, {type StringKeys} from "@/locales/useTranslation";
import React, {useEffect} from "react";
import SettingsCheckbox from "@/components/profile-screen/settingsCheckbox/SettingsCheckbox";
import styles from "./InstanceFlagsSection.module.css";

interface InstanceFlagsSectionProps {
    title?: string;
}

interface FlagCopy {
    labelKey: StringKeys;
    textKey: StringKeys;
}

const FLAG_COPY: Record<InstanceFlagType, FlagCopy> = {
    REGISTER_WITH_MAIL: {labelKey: "instanceFlagRegisterWithMail", textKey: "instanceFlagRegisterWithMailText"},
    FORGOT_PASSWORD: {labelKey: "instanceFlagForgotPassword", textKey: "instanceFlagForgotPasswordText"},
    SIGN_IN_WITH_EMAIL_CODE: {labelKey: "instanceFlagSignInWithEmailCode", textKey: "instanceFlagSignInWithEmailCodeText"},
    SIGN_IN_WITH_GOOGLE: {labelKey: "instanceFlagSignInWithGoogle", textKey: "instanceFlagSignInWithGoogleText"},
    SIGN_IN_WITH_APPLE: {labelKey: "instanceFlagSignInWithApple", textKey: "instanceFlagSignInWithAppleText"},
    WORKSPACE_INIT: {labelKey: "instanceFlagWorkspaceInit", textKey: "instanceFlagWorkspaceInitText"},
    ATTACH_GOOGLE_CALENDAR: {labelKey: "instanceFlagAttachGoogleCalendar", textKey: "instanceFlagAttachGoogleCalendarText"},
    MCP_SERVER: {labelKey: "instanceFlagMcpServer", textKey: "instanceFlagMcpServerText"}
};

const FLAG_GROUPS: { titleKey: StringKeys; flags: InstanceFlagType[] }[] = [
    {
        titleKey: "instanceSettingsFlagGroupAuth",
        flags: ["REGISTER_WITH_MAIL", "FORGOT_PASSWORD", "SIGN_IN_WITH_EMAIL_CODE", "SIGN_IN_WITH_GOOGLE", "SIGN_IN_WITH_APPLE"]
    },
    {
        titleKey: "instanceSettingsFlagGroupWorkspace",
        flags: ["WORKSPACE_INIT"]
    },
    {
        titleKey: "instanceSettingsFlagGroupIntegration",
        flags: ["ATTACH_GOOGLE_CALENDAR", "MCP_SERVER"]
    }
];

const InstanceFlagsSection: React.FC<InstanceFlagsSectionProps> = ({title}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();

    const {data: instanceFlagsResponse} = useRetrieveInstanceFlagsQuery();
    const [setInstanceFlag, {isLoading: isSetInstanceFlagLoading}] = useSetInstanceFlagMutation();

    useEffect(() => {
        dispatch(changeLoadingModalVisibility({visible: isSetInstanceFlagLoading}));
    }, [isSetInstanceFlagLoading]);

    const onFlagChange = (instanceFlagType: InstanceFlagType) => (value: boolean) => {
        setInstanceFlag({instanceFlagType, value});
    };

    return (
        <div className={styles.container}>
            {title && <h2>{title}</h2>}
            <span>
                {t('instanceFlagManagementDocsInfo')}
            </span>
            {FLAG_GROUPS.map((group) => (
                <div key={group.titleKey} className={styles.group}>
                    <h3 className={styles.groupTitle}>{t(group.titleKey)}</h3>
                    <div className={styles.flagList}>
                        {group.flags.map((flag) => (
                            <SettingsCheckbox
                                key={flag}
                                id={`instance-flag-${flag}`}
                                label={t(FLAG_COPY[flag].labelKey)}
                                text={t(FLAG_COPY[flag].textKey)}
                                checked={isInstanceFlagEnabled(instanceFlagsResponse?.data?.[flag])}
                                disabled={isSetInstanceFlagLoading}
                                onChange={onFlagChange(flag)}
                            />
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default InstanceFlagsSection;
