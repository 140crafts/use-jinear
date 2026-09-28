import {SYSTEM_CURSOR_ID} from "@/theme/themeCursors";
import {buildFontStylesheetUrl} from "@/theme/themeFonts";
import {buildThemeVariables} from "@/theme/themeVariables";
import type {ThemeAppearance, ThemeDefinition, ThemeFont, ThemePreferences} from "@/theme/themeTypes";

// index.html reads this key before the bundle loads, so a reload paints the right theme at once.
// Keep the boot script in index.html in sync with applyThemeSnapshot below.
const BOOT_SNAPSHOT_KEY = "THEME_BOOT";
const FONT_LINK_ID = "theme-font";

export interface ThemeSnapshot {
    appearance: ThemeAppearance;
    variables: Record<string, string>;
    fontHref?: string;
    cursor?: string;
}

export const buildThemeSnapshot = (theme: ThemeDefinition, font: ThemeFont, {caretShape, cursorId}: ThemePreferences): ThemeSnapshot => {
    const variables: Record<string, string> = {"--font-ui": font.family, "--caret-shape": caretShape};
    Object.entries(buildThemeVariables(theme)).forEach(([name, value]) => {
        if (value != null) {
            variables[name] = value;
        }
    });
    return {
        appearance: theme.appearance,
        variables,
        fontHref: buildFontStylesheetUrl(font),
        cursor: cursorId == SYSTEM_CURSOR_ID ? undefined : cursorId,
    };
};

const applyFontStylesheet = (href?: string) => {
    const existing = document.getElementById(FONT_LINK_ID) as HTMLLinkElement | null;
    if (!href) {
        existing?.remove();
        return;
    }
    if (existing?.href == href) {
        return;
    }
    const link = existing ?? document.createElement("link");
    link.id = FONT_LINK_ID;
    link.rel = "stylesheet";
    link.href = href;
    if (!existing) {
        document.head.appendChild(link);
    }
};

export const applyThemeSnapshot = (snapshot: ThemeSnapshot) => {
    const root = document.documentElement;
    root.classList.remove(snapshot.appearance == "dark" ? "light" : "dark");
    root.classList.add(snapshot.appearance);

    Array.from(root.style)
        .filter(name => name.startsWith("--") && !(name in snapshot.variables))
        .forEach(name => root.style.removeProperty(name));
    Object.entries(snapshot.variables).forEach(([name, value]) => root.style.setProperty(name, value));

    root.toggleAttribute("data-theme-selection", "--c-selection" in snapshot.variables);
    if (snapshot.cursor) {
        root.setAttribute("data-cursor", snapshot.cursor);
    } else {
        root.removeAttribute("data-cursor");
    }
    document.querySelector('meta[name="theme-color"]')?.setAttribute("content", snapshot.variables["--c-background"]);
    applyFontStylesheet(snapshot.fontHref);
};

export const saveBootSnapshot = (snapshot: ThemeSnapshot) => {
    try {
        localStorage.setItem(BOOT_SNAPSHOT_KEY, JSON.stringify(snapshot));
    } catch {
        // without the boot snapshot the next reload paints the default theme until React mounts
    }
};
