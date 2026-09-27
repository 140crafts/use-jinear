import {createContext} from "react";
import {DEFAULT_THEME_IDS, resolveTheme} from "@/theme/themeRegistry";
import {DEFAULT_FONT_ID} from "@/theme/themeFonts";
import type {CaretShape, ThemeDefinition, ThemePreferences} from "@/theme/themeTypes";

export interface ThemeContextValue {
    theme: ThemeDefinition;
    preferences: ThemePreferences;
    selectTheme: (themeId: string) => void;
    previewTheme: (themeId: string | null) => void;
    setFontId: (fontId: string) => void;
    setCaretShape: (caretShape: CaretShape) => void;
}

const ThemeContext = createContext<ThemeContextValue>({
    theme: resolveTheme(DEFAULT_THEME_IDS.dark),
    preferences: {
        themeId: DEFAULT_THEME_IDS.dark,
        fontId: DEFAULT_FONT_ID,
        caretShape: "auto",
    },
    selectTheme: () => {},
    previewTheme: () => {},
    setFontId: () => {},
    setCaretShape: () => {},
});

export default ThemeContext;
