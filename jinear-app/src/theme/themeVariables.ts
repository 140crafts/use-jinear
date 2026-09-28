import {getContrastRatio, getTextColor, mixHexColors} from "@/util/colorHelper";
import type {ThemeDefinition} from "@/theme/themeTypes";

// An undefined value means the theme leaves that variable to styles/variables.css.
export type ThemeVariables = Record<string, string | undefined>;

const TEXT_MUTED_WEIGHT = 0.05;
const TEXT_SUBTLE_WEIGHTS = [0.15, 0.1, 0.05];
const MIN_TEXT_CONTRAST = 4.5;

// Low contrast themes such as Solarized have little room between text and background, so fade less there.
const deriveSubtleText = (text: string, background: string): string => {
    const weight = TEXT_SUBTLE_WEIGHTS.find(candidate =>
        getContrastRatio(mixHexColors(text, background, candidate), background) >= MIN_TEXT_CONTRAST);
    return weight == null ? text : mixHexColors(text, background, weight);
};

export const buildThemeVariables = ({palette}: ThemeDefinition): ThemeVariables => {
    const rowSelection = palette.rowSelection ?? palette.accent;
    return {
        "--c-background": palette.background,
        "--c-primary": palette.background,
        "--c-primary-shade-0": palette.surface,
        "--c-primary-shade-1": palette.border,
        "--c-text-color": palette.text,
        "--c-secondary": palette.text,
        "--c-secondary-shade-0": palette.textMuted ?? mixHexColors(palette.text, palette.background, TEXT_MUTED_WEIGHT),
        "--c-secondary-shade-1": palette.textSubtle ?? deriveSubtleText(palette.text, palette.background),
        "--c-link-color": palette.link,
        "--c-link-color-inverted": palette.linkInverted ?? palette.link,
        "--c-accent": palette.accent,
        "--c-on-accent": palette.onAccent ?? getTextColor(palette.accent),
        "--c-ERROR_RED": palette.danger,
        "--c-SUCCESS_GREEN": palette.success,
        "--material-row-view-row-selection-color": rowSelection,
        "--c-on-row-selection": rowSelection == palette.accent && palette.onAccent ? palette.onAccent : getTextColor(rowSelection),
        "--c-selection": palette.selection,
        "--c-caret": palette.caret ?? palette.text,
    };
};
