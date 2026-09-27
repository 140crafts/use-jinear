import {createContext} from "react";
import {DEFAULT_THEME_IDS, resolveTheme} from "@/theme/themeRegistry";
import {SYSTEM_CURSOR_ID} from "@/theme/themeCursors";
import {DEFAULT_FONT_ID} from "@/theme/themeFonts";
import type {CaretShape, ThemeDefinition, ThemePreferences} from "@/theme/themeTypes";

export interface ThemeContextValue {
    theme: ThemeDefinition;
    preferences: ThemePreferences;
    selectTheme: (themeId: string) => void;
    previewTheme: (themeId: string | null) => void;
    setFontId: (fontId: string) => void;
    setCaretShape: (caretShape: CaretShape) => void;
    setCursorId: (cursorId: string) => void;
}

const ThemeContext = createContext<ThemeContextValue>({
    theme: resolveTheme(DEFAULT_THEME_IDS.dark),
    preferences: {
        themeId: DEFAULT_THEME_IDS.dark,
        fontId: DEFAULT_FONT_ID,
        caretShape: "auto",
        cursorId: SYSTEM_CURSOR_ID,
    },
    selectTheme: () => {},
    previewTheme: () => {},
    setFontId: () => {},
    setCaretShape: () => {},
    setCursorId: () => {},
});

export default ThemeContext;
