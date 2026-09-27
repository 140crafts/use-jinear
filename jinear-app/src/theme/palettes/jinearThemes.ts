import type {ThemeDefinition} from "@/theme/themeTypes";

export const JINEAR_CLASSIC_LIGHT_ID = "jinear-classic-light";
export const JINEAR_CLASSIC_DARK_ID = "jinear-classic-dark";

// Mirrors styles/variables.css exactly, so the classic themes look the same as before themes existed.
export const jinearThemes: ThemeDefinition[] = [
    {
        id: JINEAR_CLASSIC_LIGHT_ID,
        name: "Jinear Classic Light",
        origin: "jinear",
        appearance: "light",
        palette: {
            background: "#fdfffc",
            surface: "#f5f5f5",
            border: "#e5e5e5",
            text: "#16171a",
            textMuted: "#1f2124",
            textSubtle: "#393e41",
            link: "#122cc2",
            linkInverted: "#5fc9f8",
            accent: "#f56e0f",
            onAccent: "#fdfffc",
            danger: "#ff4b3a",
            success: "#4bb543",
            rowSelection: "#2863d9",
            caret: "#16171a",
        },
    },
    {
        id: JINEAR_CLASSIC_DARK_ID,
        name: "Jinear Classic Dark",
        origin: "jinear",
        appearance: "dark",
        palette: {
            background: "#16171a",
            surface: "#1f2124",
            border: "#393e41",
            text: "#fdfffc",
            textMuted: "#f5f5f5",
            textSubtle: "#e5e5e5",
            link: "#5fc9f8",
            linkInverted: "#122cc2",
            accent: "#f56e0f",
            onAccent: "#fdfffc",
            danger: "#ff4b3a",
            success: "#4bb543",
            rowSelection: "#2358c9",
            caret: "#fdfffc",
        },
    },
];
