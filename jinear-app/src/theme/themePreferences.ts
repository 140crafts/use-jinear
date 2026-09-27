import {DEFAULT_FONT_ID} from "@/theme/themeFonts";
import {DEFAULT_THEME_IDS, findTheme} from "@/theme/themeRegistry";
import type {ThemePreferences} from "@/theme/themeTypes";

const PREFERENCES_KEY = "THEME_PREFERENCES";
const LEGACY_THEME_KEY = "THEME";

// Only the first visit looks at the system appearance; after that the theme is what the user picked.
const defaultThemeId = (): string =>
    DEFAULT_THEME_IDS[window.matchMedia?.("(prefers-color-scheme: dark)").matches ? "dark" : "light"];

const defaultPreferences = (): ThemePreferences => ({
    themeId: defaultThemeId(),
    fontId: DEFAULT_FONT_ID,
    caretShape: "auto",
});

// Before themes, the app stored only "light" or "dark".
const migrateLegacyPreference = (): ThemePreferences | undefined => {
    const legacy = localStorage.getItem(LEGACY_THEME_KEY);
    if (legacy != "light" && legacy != "dark") {
        return undefined;
    }
    return {...defaultPreferences(), themeId: DEFAULT_THEME_IDS[legacy]};
};

export const loadThemePreferences = (): ThemePreferences => {
    try {
        const stored = localStorage.getItem(PREFERENCES_KEY);
        if (!stored) {
            return migrateLegacyPreference() ?? defaultPreferences();
        }
        const parsed: Partial<ThemePreferences> = JSON.parse(stored);
        const defaults = defaultPreferences();
        return {
            // a saved theme can disappear from the registry
            themeId: findTheme(parsed.themeId) ? parsed.themeId! : defaults.themeId,
            fontId: parsed.fontId ?? defaults.fontId,
            caretShape: parsed.caretShape ?? defaults.caretShape,
        };
    } catch {
        return defaultPreferences();
    }
};

export const saveThemePreferences = (preferences: ThemePreferences) => {
    try {
        localStorage.setItem(PREFERENCES_KEY, JSON.stringify(preferences));
        localStorage.removeItem(LEGACY_THEME_KEY);
    } catch {
        // storage can be full or blocked; the theme still applies for this session
    }
};
