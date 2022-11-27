import { useEffect, useState } from "react";
import strings from "./strings";

const LOCALES = ["tr", "en"];
const DEFAULT_LOCALE = "en";

const setLang = (lang: string) => {
  if (typeof window === "object") {
    const $html = document.querySelector("html");
    if ($html) {
      $html.lang = lang;
    }
  }
};

const getLang = () => {
  if (typeof window === "object") {
    const $html = document.querySelector("html");
    return $html?.lang;
  }
};

const useTranslation = () => {
  const [locale, setLocale] = useState(getLang() || DEFAULT_LOCALE);
  const language = LOCALES.indexOf(locale) == -1 ? DEFAULT_LOCALE : locale;
  //@ts-ignore
  const t = (key: keyof typeof strings): string => strings[key]?.[language];
  if (typeof window === "object") {
    useEffect(() => {
      const lang = navigator.language?.split("-")[0];
      setLocale(lang);
      setLang(lang);
    }, [navigator]);
  }
  return { t, language };
};

export const getTranslatedMessage = (key: keyof typeof strings) => {
  const lang = getLang() || DEFAULT_LOCALE;
  const language = LOCALES.indexOf(lang) == -1 ? DEFAULT_LOCALE : lang;
  //@ts-ignore
  return strings[key]?.[language];
};

export default useTranslation;
