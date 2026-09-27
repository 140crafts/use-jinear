import type {ThemeFont} from "@/theme/themeTypes";

export const DEFAULT_FONT_ID = "inter";

// Inter is already loaded by styles/fonts.css. The other Google fonts load on demand, only when a user picks them.
export const THEME_FONTS: ThemeFont[] = [
    {id: DEFAULT_FONT_ID, name: "Inter", family: `"Inter", sans-serif`},
    {id: "system", name: "System", family: `system-ui, -apple-system, "Segoe UI", Roboto, sans-serif`},
    {id: "ibm-plex-sans", name: "IBM Plex Sans", family: `"IBM Plex Sans", sans-serif`, googleFontsFamily: "IBM+Plex+Sans:wght@300;400;500;600;700"},
    {id: "roboto", name: "Roboto", family: `"Roboto", sans-serif`, googleFontsFamily: "Roboto:wght@300;400;500;600;700"},
    {id: "nunito-sans", name: "Nunito Sans", family: `"Nunito Sans", sans-serif`, googleFontsFamily: "Nunito+Sans:wght@300;400;500;600;700"},
    {id: "lexend", name: "Lexend", family: `"Lexend", sans-serif`, googleFontsFamily: "Lexend:wght@300;400;500;600;700"},
    {id: "atkinson-hyperlegible", name: "Atkinson Hyperlegible", family: `"Atkinson Hyperlegible Next", sans-serif`, googleFontsFamily: "Atkinson+Hyperlegible+Next:wght@300;400;500;600;700"},
    {id: "jetbrains-mono", name: "JetBrains Mono", family: `"JetBrains Mono", ui-monospace, monospace`, googleFontsFamily: "JetBrains+Mono:wght@300;400;500;600;700"},
    {id: "fira-code", name: "Fira Code", family: `"Fira Code", ui-monospace, monospace`, googleFontsFamily: "Fira+Code:wght@300;400;500;600;700"},
];

export const findFont = (fontId?: string | null): ThemeFont =>
    THEME_FONTS.find(font => font.id == fontId) ?? THEME_FONTS[0];

export const buildFontStylesheetUrl = (font: ThemeFont): string | undefined =>
    font.googleFontsFamily ? `https://fonts.googleapis.com/css2?family=${font.googleFontsFamily}&display=swap` : undefined;
