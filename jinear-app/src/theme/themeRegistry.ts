import {JINEAR_CLASSIC_DARK_ID, JINEAR_CLASSIC_LIGHT_ID, jinearThemes} from "@/theme/palettes/jinearThemes";
import {jetbrainsThemes} from "@/theme/palettes/jetbrainsThemes";
import {popularThemes} from "@/theme/palettes/popularThemes";
import {vscodeThemes} from "@/theme/palettes/vscodeThemes";
import type {ThemeAppearance, ThemeDefinition, ThemeOrigin} from "@/theme/themeTypes";

export const THEME_ORIGINS: ThemeOrigin[] = ["jinear", "vscode", "jetbrains", "popular"];

export const ALL_THEMES: ThemeDefinition[] = [...jinearThemes, ...vscodeThemes, ...jetbrainsThemes, ...popularThemes];

const THEMES_BY_ID = new Map(ALL_THEMES.map(theme => [theme.id, theme]));

export const DEFAULT_THEME_IDS: Record<ThemeAppearance, string> = {
    light: JINEAR_CLASSIC_LIGHT_ID,
    dark: JINEAR_CLASSIC_DARK_ID,
};

export const findTheme = (themeId?: string | null): ThemeDefinition | undefined =>
    themeId ? THEMES_BY_ID.get(themeId) : undefined;

export const resolveTheme = (themeId: string | null | undefined): ThemeDefinition =>
    findTheme(themeId) ?? THEMES_BY_ID.get(DEFAULT_THEME_IDS.light)!;
