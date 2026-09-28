import type {StringKeys} from "@/locales/useTranslation";

export const SYSTEM_CURSOR_ID = "system";

// Every id other than the system one needs a matching [data-cursor] block in styles/cursors.css.
export const THEME_CURSORS: { id: string; labelKey: StringKeys }[] = [
    {id: SYSTEM_CURSOR_ID, labelKey: "themePickerCursorSystem"},
    {id: "pati", labelKey: "themePickerCursorPati"},
    {id: "dog", labelKey: "themePickerCursorDog"},
    {id: "duck", labelKey: "themePickerCursorDuck"},
    {id: "wand", labelKey: "themePickerCursorWand"},
    {id: "pixel", labelKey: "themePickerCursorPixel"},
    {id: "mitten", labelKey: "themePickerCursorMitten"},
];

export const isKnownCursor = (cursorId?: string | null): boolean =>
    THEME_CURSORS.some(cursor => cursor.id == cursorId);
