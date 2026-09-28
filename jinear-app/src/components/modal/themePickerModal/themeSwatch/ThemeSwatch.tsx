import type {ThemeDefinition} from "@/theme/themeTypes";
import React from "react";
import styles from "./ThemeSwatch.module.css";

interface ThemeSwatchProps {
    theme: ThemeDefinition;
}

// A tiny app preview drawn with the theme's own colors, so it reads correctly under any active theme.
const ThemeSwatch: React.FC<ThemeSwatchProps> = ({theme: {palette}}) => {
    return (
        <span className={styles.container} style={{backgroundColor: palette.background, borderColor: palette.border}}>
            <span className={styles.sidebar} style={{backgroundColor: palette.surface}}/>
            <span className={styles.content}>
                <span className={styles.line} style={{backgroundColor: palette.text}}/>
                <span className={styles.shortLine} style={{backgroundColor: palette.link}}/>
                <span className={styles.accent} style={{backgroundColor: palette.accent}}/>
            </span>
        </span>
    );
};

export default ThemeSwatch;
