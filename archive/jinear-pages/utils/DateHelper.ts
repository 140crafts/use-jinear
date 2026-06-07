import enUsLocale from "date-fns/locale/en-US";
import trTrLocale from "date-fns/locale/tr";
import { getTranslatedMessage } from "locales/useTranslation";

export const dateFnsLocale = getTranslatedMessage("localeType") == "TR" ? trTrLocale : enUsLocale;
