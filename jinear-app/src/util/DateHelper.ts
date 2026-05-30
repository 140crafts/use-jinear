import {enUS} from "date-fns/locale/en-US";
import {tr} from "date-fns/locale/tr";
import {getTranslatedMessage} from "@/locales/useTranslation";
import {differenceInDays, differenceInHours, differenceInMinutes} from "date-fns";

export const dateFnsLocale = getTranslatedMessage("localeType") == "TR" ? tr : enUS;

export const calculateDateDiff = (dateToUse: string | Date) => {
    try {
        if (dateToUse) {
            const parsed = new Date(dateToUse);
            const diffInDays = differenceInDays(new Date(), parsed);
            if (diffInDays != 0) {
                return getTranslatedMessage("dateDiffLabelDateInDays")?.replace("${num}", `${diffInDays}`);
            }
            const diffInHours = differenceInHours(new Date(), parsed);
            if (diffInHours != 0) {
                return getTranslatedMessage("dateDiffLabelDateInHours")?.replace("${num}", `${diffInHours}`);
            }
            const diffInMinutes = differenceInMinutes(new Date(), parsed);
            if (diffInMinutes != 0) {
                return getTranslatedMessage("dateDiffLabelDateInMinutes")?.replace("${num}", `${diffInMinutes}`);
            }
            return getTranslatedMessage("dateDiffLabelDateJustNow");
        }
        return "";
    } catch (e) {
        console.log(e);
        return "";
    }
};