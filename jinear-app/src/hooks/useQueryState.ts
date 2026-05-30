import { tryCatch } from "@/util/tryCatch";
import { createUrl } from "@/util/urlUtils";
import { format, formatISO, parse, parseISO } from "date-fns";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

export function useQueryState<T>(key: string, parser?: (value?: string) => T | undefined): T | undefined | null {
  const [searchParams] = useSearchParams();
  const _val = searchParams.get(key);
  return _val && parser ? parser(_val) : (_val as T);
}

export const useSetQueryState = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [searchParams] = useSearchParams();
  return (key: string, val?: string) => {
    const urlSearchParams = new URLSearchParams(searchParams);
    val ? urlSearchParams.set(key, val) : urlSearchParams.delete(key);
    navigate(createUrl(pathname, urlSearchParams), { preventScrollReset: true });
  };
};

export const useSetQueryStateMultiple = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();
  const [searchParams] = useSearchParams();
  return (pairs: Map<string, string | undefined>) => {
    const urlSearchParams = new URLSearchParams(searchParams);
    pairs.forEach((value, key) => {
      value ? urlSearchParams.set(key, value) : urlSearchParams.delete(key);
    });
    navigate(createUrl(pathname, urlSearchParams), { preventScrollReset: true });
  };
};

const URL_DATE_FORMAT = "yyyy-dd-MM";

export const queryStateIntParser = (val?: string) => (val ? parseInt(val) : undefined);
export const queryStateArrayParser = <T>(val?: string) => (val ? (val.split(",") as T) : undefined);
export const queryStateIsoDateParser = (val?: string) => (val ? parseISO(val) : undefined);
export const queryStateShortDateParser = (val?: string) => {
  if (val) {
    const result = tryCatch(() => parse(val, URL_DATE_FORMAT, new Date())).result;
    return result && !isNaN(result.getTime()) ? result : undefined;
  }
  return undefined;
};
export const queryStateBooleanParser = (val?: string) => (val ? val?.toLowerCase() == "true" : undefined);
export const queryStateJsonObjectParser = <T>(val?: string) => {
  if (val) {
    try {
      return (JSON.parse(val) as T);
    } catch (e) {
      console.error(e);
      return undefined;
    }
  }
  return undefined;
};

export const queryStateDateToIsoDateConverter = (date?: Date | null) => (date ? formatISO(date) : undefined);
export const queryStateDateToShortDateConverter = (date?: Date | null) => (date ? format(date, URL_DATE_FORMAT) : undefined);
export const queryStateAnyToStringConverter = (obj?: any) => (obj ? obj.toString() : undefined);
export const queryStateObjectToJsonStringConverter = (obj?: any) => (obj ? JSON.stringify(obj) : undefined);