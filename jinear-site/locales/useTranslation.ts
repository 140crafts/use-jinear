"use client";
// Marketing site is English-only for now (Turkish deferred). This is a trimmed
// version of jinear-web's hook: it always resolves the "en" value, so static
// HTML is fully populated at build time with no client-side language switching.
import strings from "./strings";

const DEFAULT_LOCALE = "en";

const useTranslation = () => {
  //@ts-ignore - strings is a large untyped object map
  const t = (key: keyof typeof strings): string => strings[key]?.[DEFAULT_LOCALE];
  return { t, language: DEFAULT_LOCALE };
};

export const getTranslatedMessage = (key: keyof typeof strings) => {
  //@ts-ignore
  return strings[key]?.[DEFAULT_LOCALE];
};

export default useTranslation;

export type TranslationKey = keyof typeof strings;
