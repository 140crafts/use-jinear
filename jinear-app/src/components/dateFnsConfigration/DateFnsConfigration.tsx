import {setDefaultOptions} from "date-fns";
import {enUS} from "date-fns/locale/en-US";
import {tr} from "date-fns/locale/tr";

import useTranslation from "@/locales/useTranslation";
import React from "react";

interface DateFnsConfigrationProps {
}

const DateFnsConfigration: React.FC<DateFnsConfigrationProps> = ({}) => {
    const {t} = useTranslation();

    const dateFnsLocale = t("localeType") == "TR" ? tr : enUS;
    setDefaultOptions({locale: dateFnsLocale});

    return null;
};

export default DateFnsConfigration;
