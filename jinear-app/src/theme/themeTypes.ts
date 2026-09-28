export type ThemeAppearance = "light" | "dark";

export type ThemeOrigin = "jinear" | "vscode" | "jetbrains" | "popular";

export type CaretShape = "auto" | "bar" | "block" | "underscore";

/**
 * A theme is plain data so it can be serialized and shared later.
 * Optional colors are derived from the required ones when a theme leaves them out.
 */
export interface ThemePalette {
    background: string;
    surface: string;
    border: string;
    text: string;
    textMuted?: string;
    textSubtle?: string;
    link: string;
    linkInverted?: string;
    accent: string;
    onAccent?: string;
    danger?: string;
    success?: string;
    rowSelection?: string;
    selection?: string;
    caret?: string;
}

export interface ThemeDefinition {
    id: string;
    name: string;
    origin: ThemeOrigin;
    appearance: ThemeAppearance;
    palette: ThemePalette;
}

export interface ThemeFont {
    id: string;
    name: string;
    family: string;
    googleFontsFamily?: string;
}

export interface ThemePreferences {
    themeId: string;
    fontId: string;
    caretShape: CaretShape;
    cursorId: string;
}
