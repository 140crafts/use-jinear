import {useEffect, useState} from "react";
import strings from "./strings";

const LOCALES = ["tr", "en"] as const;
const DEFAULT_LOCALE: Language = "en";

type Language = typeof LOCALES[number];

export type StringKeys = {
    [K in keyof typeof strings]: typeof strings[K] extends Record<Language, string> ? K : never
}[keyof typeof strings];

const setHtmlLang = (lang: string) => {
    const $html = document.querySelector("html");
    if ($html) {
        $html.lang = lang;
    }
};

const getHtmlLang = () => document.querySelector("html")?.lang;

const resolveLanguage = (locale: string | undefined): Language =>
    (LOCALES as readonly string[]).includes(locale ?? "")
        ? (locale as Language)
        : DEFAULT_LOCALE;

const detectInitialLocale = (): string =>
    getHtmlLang() || navigator.language?.split("-")[0] || DEFAULT_LOCALE;

const useTranslation = () => {
    const [locale] = useState<string>(detectInitialLocale);
    const language = resolveLanguage(locale);
    const t = (key: StringKeys): string => strings[key]?.[language];

    useEffect(() => {
        setHtmlLang(language);
    }, [language]);

    return {t, language};
};

export const getTranslatedMessage = (key: StringKeys): string => {
    const language = resolveLanguage(getHtmlLang());
    return strings[key]?.[language];
};

export default useTranslation;

export type TranslationKey = keyof typeof strings;
