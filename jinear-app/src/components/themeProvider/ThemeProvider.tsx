import ThemeContext from "@/store/context/themeContext";
import {applyThemeSnapshot, buildThemeSnapshot, saveBootSnapshot} from "@/theme/applyTheme";
import {findFont} from "@/theme/themeFonts";
import {loadThemePreferences, saveThemePreferences} from "@/theme/themePreferences";
import {findTheme, resolveTheme} from "@/theme/themeRegistry";
import type {CaretShape, ThemePreferences} from "@/theme/themeTypes";
import {submitThemeChangeWebviewEvent} from "@/util/webviewUtils";
import React, {useContext, useEffect, useState} from "react";

interface ThemeProviderProps {
    children: React.ReactNode;
}

export function useThemeSettings() {
    return useContext(ThemeContext);
}

const ThemeProvider: React.FC<ThemeProviderProps> = ({children}) => {
    const [preferences, setPreferences] = useState<ThemePreferences>(loadThemePreferences);
    const [previewThemeId, setPreviewThemeId] = useState<string | null>(null);

    const savedTheme = resolveTheme(preferences.themeId);
    const theme = findTheme(previewThemeId) ?? savedTheme;

    useEffect(() => {
        applyThemeSnapshot(buildThemeSnapshot(theme, findFont(preferences.fontId), preferences.caretShape));
    }, [theme, preferences.fontId, preferences.caretShape]);

    useEffect(() => {
        saveThemePreferences(preferences);
        saveBootSnapshot(buildThemeSnapshot(savedTheme, findFont(preferences.fontId), preferences.caretShape));
        submitThemeChangeWebviewEvent(savedTheme.appearance);
    }, [preferences, savedTheme]);

    const selectTheme = (themeId: string) => {
        setPreviewThemeId(null);
        setPreferences(current => ({...current, themeId}));
    };

    const setFontId = (fontId: string) => setPreferences(current => ({...current, fontId}));
    const setCaretShape = (caretShape: CaretShape) => setPreferences(current => ({...current, caretShape}));

    return (
        <ThemeContext.Provider
            value={{
                theme,
                preferences,
                selectTheme,
                previewTheme: setPreviewThemeId,
                setFontId,
                setCaretShape,
            }}>
            {children}
        </ThemeContext.Provider>
    );
};

export default ThemeProvider;
