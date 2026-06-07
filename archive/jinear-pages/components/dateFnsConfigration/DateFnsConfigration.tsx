"use client";
import { setDefaultOptions } from "date-fns";
import enUsLocale from "date-fns/locale/en-US";
import trTrLocale from "date-fns/locale/tr";
import useTranslation from "locales/useTranslation";
import React from "react";

interface DateFnsConfigrationProps {}

const DateFnsConfigration: React.FC<DateFnsConfigrationProps> = ({}) => {
  const { t } = useTranslation();

  const dateFnsLocale = t("localeType") == "TR" ? trTrLocale : enUsLocale;
  setDefaultOptions({ locale: dateFnsLocale });

  return null;
};

export default DateFnsConfigration;
